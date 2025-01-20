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
import zio._
import zio.logging._
import zio.logging.LogFormat._

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> consoleLogger(
      ConsoleLoggerConfig(
        text("[") |-| level.color(LogColor.MAGENTA) |-| text("] ") |-|
          text("dmtscala - ") |-|
          timestamp.fixed(32).color(LogColor.MAGENTA) |-| text(" - ") |-|
          line.color(LogColor.WHITE).highlight +
          (space + label("cause", cause).highlight)
            .filter(LogFilter.causeNonEmpty),
        LogFilter.LogLevelByNameConfig.default
      )
    )

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {
    (for {
      args <- ZIO.service[ZIOAppArgs]
      _    <-
        OParser
          .parse(dmtConfigParser, args.getArgs, DMTConfig()) match {
          case Some(config) => (runDMT(config)).exitCode
          case _            => ZIO.unit
        }
      _    <- ZIO.logInfo("")
    } yield ()).exitCode
  }
}
