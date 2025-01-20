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

import io.github.jjba23.dmtscala.Python._
import io.github.jjba23.dmtscala.Python
import io.github.jjba23.dmtscala.DMT
import scala.meta._
import zio.test._

object PythonSpec extends ZIOSpecDefault {

  val typeExpectations: Seq[(String, String)] = Seq(
    "String"                 -> "str",
    "Option[String]"         -> "Optional[str]",
    "Seq[String]"            -> "List[str]",
    "Option[Seq[String]]"    -> "Optional[List[str]]",
    "List[String]"           -> "List[str]",
    "Option[List[String]]"   -> "Optional[List[str]]",
    "Boolean"                -> "bool",
    "Option[Boolean]"        -> "Optional[bool]",
    "Int"                    -> "int",
    "Option[Int]"            -> "Optional[int]",
    "Seq[Int]"               -> "List[int]",
    "Option[Seq[Int]]"       -> "Optional[List[int]]",
    "List[Int]"              -> "List[int]",
    "Option[List[Int]]"      -> "Optional[List[int]]",
    "Seq[SomeClass]"         -> "List[SomeClass]",
    "List[SomeClass]"        -> "List[SomeClass]",
    "Option[Seq[SomeClass]]" -> "Optional[List[SomeClass]]"
  )
  val nameExpectations: Seq[(String, String)] = Seq(
    "someField"   -> "someField",
    "`type`"      -> "type",
    "`someField`" -> "someField"
  )

  def spec = suite("Python")(
    test("toPythonType should perform translation properly") {
      assertTrue(
        typeExpectations
          .map(p => toPythonType(p._1) == p._2)
          .forall(_ == true)
      )
    },
    test("toPythonName should perform translation properly") {
      assertTrue(
        nameExpectations
          .map(p => toPythonName(p._1) == p._2)
          .forall(_ == true)
      )
    },
    test("serialize should properly generate a Typescript interface") {
      val scalaClass = """
  case class MyDataClass(id: Option[UUID], numList: Seq[Int])
  """.parse[Stat].get

      val parsedFields = scalaClass match {
        case someClass: Defn.Class =>
          someClass.ctor.children
            .collect { case tree: Tree =>
              DMT.extractFieldsFromClass(tree)
            }
            .flatten
            .map(DMT.classFieldToModelField)
        case _                     => Seq.empty
      }

      val sut = Python
        .DataClass(
          name = "MyDataClass",
          fields = parsedFields
        )
        .toString

      assertTrue(
        sut == "@dataclass\nclass MyDataClass:\n    id: Optional[str]\n    numList: List[int]"
      )
    }
  )
}
