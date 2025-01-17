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

import scala.io.AnsiColor._
import java.time.Instant

object Logger {

  def log(level: String)(msg: String): Unit = {
    val now: Long         = Instant.now().getEpochSecond()
    val colorCode: String = level match {
      case "info"    => MAGENTA
      case "error"   => RED
      case "warning" => YELLOW
    }

    println(s"$colorCode>>= [$level] - $now - $msg$RESET")

  }

  val logInfo: String => Unit    = (msg: String) => log("info")(msg)
  val logWarning: String => Unit = (msg: String) => log("warning")(msg)
  val logError: String => Unit   = (msg: String) => log("error")(msg)
  val logRaw: String => Unit     = (msg: String) => println(s"\n$msg\n")
}
