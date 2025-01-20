// dmtscala is the Data Model Translator for Scala

// Copyright © 2025 "Josep Bigorra" known as "jjba23" <jjbigorra@gmail.com>

// dmtscala is free software: you can redistribute it and/or modify
// it under the terms of the Lesser GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// dmtscala is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the Lesser GNU General Public License
// along with dmtscala.  If not, see <https://www.gnu.org/licenses/>.

package io.github.jjba23.dmtscala

import zio._
import scala.meta._
import scala.io.{Source => FileSource}
import Logger._
import java.io.File
import java.io.PrintWriter
import Haskell.{serialize => serializeHaskell}
import Typescript.{serialize => serializeTypescript}
import Python.{serialize => serializePython}

case class ModelField(name: String, typ: String)

case class DMTModel(
  name: String,
  scala: Tree,
  typescriptInterface: Option[Typescript.Interface] = None,
  haskellRecord: Option[Haskell.Record] = None,
  pythonDataClass: Option[Python.DataClass] = None
)

object DMT {

  val extractFieldsFromClass: Tree => Seq[Term.Param] = (x: Tree) => {
    x match {
      case x: Term.ParamClause => x.values
      case _                   => Seq.empty
    }
  }

  val classFieldToModelField: Term.Param => ModelField = x =>
    ModelField(
      name = x.name.toString,
      typ = x.decltpe.map(_.toString).getOrElse("")
    )

  def extractDataModelFromTree(
    config: DMTConfig,
    tree: Tree
  ): Option[DMTModel] = {
    tree match {
      case someClass: Defn.Class => {
        if (!config.silent)
          logInfo(s"detected class of name: ${someClass.name}")

        val m: Seq[Term.Param] = someClass.ctor.children.collect {
          case tree: Tree =>
            extractFieldsFromClass(tree)
        }.flatten

        if (!config.silent) logInfo("Scala model:")
        if (!config.silent) logRaw(someClass.toString)

        val typescriptInterface: Option[Typescript.Interface] =
          if (config.toTypescript) {
            val ts = Typescript.Interface(
              name = someClass.name.toString,
              fields = m.map(classFieldToModelField)
            )
            if (!config.silent) logInfo("Typescript interface:")
            if (!config.silent) logRaw(ts.toString)
            Some(ts)
          } else { None }

        val haskellRecord: Option[Haskell.Record] =
          if (config.toHaskell) {
            val hs = Haskell.Record(
              name = someClass.name.toString,
              fields = m.map(classFieldToModelField)
            )
            if (!config.silent) logInfo("Haskell record:")
            if (!config.silent) logRaw(hs.toString)
            Some(hs)
          } else { None }

        val pythonDataClass: Option[Python.DataClass] =
          if (config.toPython) {
            val py = Python.DataClass(
              name = someClass.name.toString,
              fields = m.map(classFieldToModelField)
            )
            if (!config.silent) logInfo("Python dataclass:")
            if (!config.silent) logRaw(py.toString)
            Some(py)
          } else { None }

        Some(
          DMTModel(
            name = someClass.name.toString,
            scala = someClass,
            typescriptInterface = typescriptInterface,
            haskellRecord = haskellRecord,
            pythonDataClass = pythonDataClass
          )
        )
      }
      case _                     => None
    }
  }

  def readTreeFromFile(filePath: String): Task[Source] =
    ZIO.attempt(FileSource.fromFile(filePath).mkString.parse[Source].get)

  def writeToFile(path: String)(data: String): Task[Unit] = ZIO.attempt {
    val pw = new PrintWriter(new File(path))
    try pw.write(data)
    finally pw.close()
  }

  def runDMTOnFile(config: DMTConfig)(file: File): Task[Unit] = for {
    _ <- ZIO
           .logInfo(s"parsing syntax tree for ${file.getAbsolutePath()}")
           .unless(config.silent)

    treeData <- readTreeFromFile(file.getAbsolutePath())
    models    = treeData
                  .collect { case tree =>
                    extractDataModelFromTree(config = config, tree = tree)
                  }
                  .collect { case Some(v) => v }

    _ <- ZIO
           .logInfo("saving translations to files")
           .unless(config.silent)

    _ <- ZIO.when(config.toTypescript) {
           val typescriptFile =
             file.getAbsolutePath().replaceAll("\\.scala", ".ts")
           val tsModels       = models
             .map(dmtModel => dmtModel.typescriptInterface)
             .collect { case Some(v) => v }
           writeToFile(typescriptFile)(serializeTypescript(tsModels))
         }

    _ <- ZIO.when(config.toHaskell) {
           val haskellFile =
             file.getAbsolutePath().replaceAll("\\.scala", ".hs")
           val hsModels    = models
             .map(dmtModel => dmtModel.haskellRecord)
             .collect { case Some(v) => v }
           writeToFile(haskellFile)(serializeHaskell(hsModels))
         }

    _ <- ZIO.when(config.toPython) {
           val pythonFile = file.getAbsolutePath().replaceAll("\\.scala", ".py")
           val pyModels   = models
             .map(dmtModel => dmtModel.pythonDataClass)
             .collect { case Some(v) => v }
           writeToFile(pythonFile)(serializePython(pyModels))
         }

    _ <- ZIO
           .logInfo("finalized saving translations to files")
           .unless(config.silent)

  } yield ()

  def runDMT(config: DMTConfig): Task[Unit] =
    ZIO.foreachDiscard(config.withInputs)(runDMTOnFile(config))

}
