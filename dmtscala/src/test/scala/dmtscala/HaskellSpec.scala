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

import io.github.jjba23.dmtscala.Haskell
import io.github.jjba23.dmtscala.Haskell._
import io.github.jjba23.dmtscala.DMT
import scala.meta._
import zio.test._

object HaskellSpec extends ZIOSpecDefault {

  val typeExpectations: Seq[(String, String)] = Seq(
    "String"                 -> "String",
    "Option[String]"         -> "Maybe String",
    "Seq[String]"            -> "[String]",
    "Option[Seq[String]]"    -> "Maybe [String]",
    "List[String]"           -> "[String]",
    "Option[List[String]]"   -> "Maybe [String]",
    "Boolean"                -> "Bool",
    "Option[Boolean]"        -> "Maybe Bool",
    "Int"                    -> "Int",
    "Option[Int]"            -> "Maybe Int",
    "Seq[Int]"               -> "[Int]",
    "Option[Seq[Int]]"       -> "Maybe [Int]",
    "List[Int]"              -> "[Int]",
    "Option[List[Int]]"      -> "Maybe [Int]",
    "Seq[SomeClass]"         -> "[SomeClass]",
    "List[SomeClass]"        -> "[SomeClass]",
    "Option[Seq[SomeClass]]" -> "Maybe [SomeClass]"
  )
  val nameExpectations: Seq[(String, String)] = Seq(
    "someField"   -> "someField",
    "`type`"      -> "type",
    "`someField`" -> "someField"
  )

  def spec = suite("Haskell")(
    test("toHaskellType should perform translation properly") {
      assertTrue(
        typeExpectations
          .map(p => toHaskellType(p._1) == p._2)
          .forall(_ == true)
      )
    },
    test("toHaskellName should perform translation properly") {
      assertTrue(
        nameExpectations
          .map(p => toHaskellName(p._1) == p._2)
          .forall(_ == true)
      )
    },
    test("serialize should properly generate a Haskell interface") {
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

      val sut = Haskell
        .Record(
          name = "MyDataClass",
          fields = parsedFields
        )
        .toString

      assertTrue(
        sut == "data MyDataClass = MyDataClass{\n    id :: Maybe UUID,\n    numList :: [Int],\n} deriving (Eq, Show, Generic, FromJSON, ToJSON)\n\nmakeFieldLabelsNoPrefix ''MyDataClass\n"
      )
    }
  )
}
