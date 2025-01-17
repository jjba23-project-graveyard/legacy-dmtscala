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

import scopt.OParser
import scopt.OParserBuilder
import java.io.File

object CLIConfig {

  val dmtConfigParserBuilder: OParserBuilder[DMTConfig] =
    OParser.builder[DMTConfig]

  val dmtConfigParser = {
    import dmtConfigParserBuilder._
    OParser.sequence(
      programName("dmtscala"),
      head(
        "\ndmtscala is the Data Model Translator from Scala to multiple other languages\n"
      ),
      version("version").text("view dmtscala version details"),
      help('h', "help").text("view dmtscala detailed usage information"),
      opt[Boolean]("silent")
        .action((x, c) => c.copy(silent = x))
        .valueName("")
        .text("silent flag will ensure dmtscala does not output any logging"),
      opt[Boolean]("to-python")
        .action((x, c) => c.copy(toPython = x))
        .valueName("")
        .text("to-python flag enables Python data model translation"),
      opt[Boolean]("to-typescript")
        .action((x, c) => c.copy(toTypescript = x))
        .valueName("")
        .text("to-typescript flag enables Typescript data model translation"),
      opt[Boolean]("to-haskell")
        .action((x, c) => c.copy(toHaskell = x))
        .valueName("")
        .text("to-haskell flag enables Haskell data model translation"),
      opt[Seq[File]]("with-inputs")
        .required()
        .valueName("<file1>,<file2>,...")
        .action((x, c) => c.copy(withInputs = x))
        .text(
          "with-inputs specifies the Scala files to be read for translation"
        ),
      checkConfig(c =>
        if (!c.toTypescript && !c.toPython && !c.toHaskell) {
          failure(
            "you should at least activate one programming language to translate to!"
          )
        } else { success }
      )
    )
  }
}

case class DMTConfig(
  debug: Boolean = false,
  silent: Boolean = false,
  toPython: Boolean = false,
  toTypescript: Boolean = false,
  toHaskell: Boolean = false,
  withInputs: Seq[File] = Seq()
)
