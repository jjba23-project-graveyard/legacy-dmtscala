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

import org.scalatest._
import flatspec._
import matchers._
import io.github.jjba23.dmtscala.Typescript._
import io.github.jjba23.dmtscala.Typescript
import io.github.jjba23.dmtscala.DMT
import scala.meta._

class TypescriptSpec extends AnyFlatSpec with should.Matchers {

  "toTypescriptType" should "translate types properly" in {

    val xs: Seq[(String, String)] = Seq(
      "String"                 -> "string",
      "Option[String]"         -> "string",
      "Seq[String]"            -> "string[]",
      "Option[Seq[String]]"    -> "string[]",
      "List[String]"           -> "string[]",
      "Option[List[String]]"   -> "string[]",
      "Boolean"                -> "boolean",
      "Option[Boolean]"        -> "boolean",
      "Int"                    -> "number",
      "Option[Int]"            -> "number",
      "Seq[Int]"               -> "number[]",
      "Option[Seq[Int]]"       -> "number[]",
      "List[Int]"              -> "number[]",
      "Option[List[Int]]"      -> "number[]",
      "Seq[SomeClass]"         -> "SomeClass[]",
      "List[SomeClass]"        -> "SomeClass[]",
      "Option[Seq[SomeClass]]" -> "SomeClass[]"
    )
    xs.foreach { case (_, v) => toTypescriptType(v) should be(v) }
  }

  "toTypescriptName" should "translate names properly" in {
    val xs: Seq[(String, String)] = Seq(
      "someField"   -> "someField",
      "`type`"      -> "type",
      "`someField`" -> "someField"
    )
    xs.foreach { case (k, v) => toTypescriptName((k, v)) should be(v) }
  }

  "serialize" should "properly generate a Typescript interface" in {
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

    val sut = Typescript
      .Interface(
        name = "MyInterface",
        fields = parsedFields
      )
      .toString

    sut should be(
      "export interface MyInterface {\n\tid?: string;\n\tnumList: number[];\n}"
    )
  }
}
