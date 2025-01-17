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

import DMT._
import scopt.OParser
import CLIConfig._

object Main {

  def main(args: Array[String]) = {

    OParser.parse(dmtConfigParser, args, DMTConfig()) match {
      case Some(config) => runDMT(config)
      case _            =>
      // arguments are bad, error message will have been displayed
    }
  }

}
