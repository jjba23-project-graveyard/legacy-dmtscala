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
import io.github.jjba23.dmtscala.Python._
import io.github.jjba23.dmtscala.Python
import io.github.jjba23.dmtscala.ModelField

class PythonSpec extends AnyFlatSpec with should.Matchers {

  "toPythonType" should "translate types properly" in {

    val xs: Seq[(String, String)] = Seq(
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
    xs.foreach { case (k, v) => toPythonType(k) should be(v) }
  }

  "toPythonName" should "translate names properly" in {
    val xs: Seq[(String, String)] = Seq(
      "someField"   -> "someField",
      "`type`"      -> "type",
      "`someField`" -> "someField"
    )
    xs.foreach { case (k, v) => toPythonName(k) should be(v) }
  }

  "serialize" should "properly generate a Python interface" in {
    val sut = Python
      .DataClass(
        name = "MyInterface",
        fields = Seq(
          ModelField(name = "id", typ = "Option[UUID]"),
          ModelField(name = "numList", typ = "Seq[Int]")
        )
      )
      .toString

    sut should be(
      "@dataclass\nclass MyInterface:\n\tid:Optional[str]\n\tnumList:List[int]"
    )
  }
}
