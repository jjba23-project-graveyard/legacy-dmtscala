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

  def extractClass(tree: Tree): Option[Defn.Class] = tree match {
    case someClass: Defn.Class => {
      Some(someClass)
    }
    case _                     => None
  }

  def extractDataModelFromTree(
    config: DMTConfig,
    someClass: Defn.Class
  ): Task[DMTModel] = for {
    _                 <- ZIO
                           .logInfo(s"detected class of name: ${someClass.name}")
                           .unless(config.silent)

    m: Seq[Term.Param] = someClass.ctor.children.collect { case tree: Tree =>
                           extractFieldsFromClass(tree)
                         }.flatten

    _ <- ZIO.logInfo("Scala model:").unless(config.silent)
    _ <- ZIO.logInfo(someClass.toString).unless(config.silent)

    typescriptInterface <-
      if (config.toTypescript) {
        val ts = Typescript.Interface(
          name = someClass.name.toString,
          fields = m.map(classFieldToModelField)
        )
        for {
          _ <- ZIO.logInfo("Typescript interface:").unless(config.silent)
          _ <- ZIO.logInfo(ts.toString).unless(config.silent)
        } yield (Some(ts))
      } else { ZIO.none }

    haskellRecord <-
      if (config.toHaskell) {
        val hs = Haskell.Record(
          name = someClass.name.toString,
          fields = m.map(classFieldToModelField)
        )
        for {
          _ <- ZIO.logInfo("Haskell record:").unless(config.silent)
          _ <- ZIO.logInfo(hs.toString).unless(config.silent)
        } yield (Some(hs))
      } else { ZIO.none }

    pythonDataClass <-
      if (config.toPython) {
        val py = Python.DataClass(
          name = someClass.name.toString,
          fields = m.map(classFieldToModelField)
        )
        for {
          _ <- ZIO.logInfo("Python dataclass:").unless(config.silent)
          _ <- ZIO.logInfo(py.toString).unless(config.silent)
        } yield (Some(py))
      } else { ZIO.none }

  } yield (DMTModel(
    name = someClass.name.toString,
    scala = someClass,
    typescriptInterface = typescriptInterface,
    haskellRecord = haskellRecord,
    pythonDataClass = pythonDataClass
  ))

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
    classes   = treeData
                  .collect { case tree => extractClass(tree) }
                  .collect { case Some(v) =>
                    v
                  }
                  .toSeq

    models <- ZIO.collectAll(classes.map { someClass =>
                extractDataModelFromTree(
                  config = config,
                  someClass = someClass
                )
              })

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
