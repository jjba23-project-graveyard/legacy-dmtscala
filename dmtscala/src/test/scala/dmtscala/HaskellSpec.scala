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
import io.github.jjba23.dmtscala.Haskell._

class HaskellSpec extends AnyFlatSpec with should.Matchers {

  "toHaskellType" should "translate types properly" in {

    val xs: Seq[(String, String)] = Seq(
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
    xs.foreach { case (k, v) => toHaskellType(k) should be(v) }
  }

  "toHaskellName" should "translate names properly" in {
    val xs: Seq[(String, String)] = Seq(
      "someField"   -> "someField",
      "`type`"      -> "type",
      "`someField`" -> "someField"
    )
    xs.foreach { case (k, v) => toHaskellName(k) should be(v) }
  }
}
