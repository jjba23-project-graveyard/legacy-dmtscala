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

import sbt.Keys._
import sbt._

object Dependencies extends AutoPlugin {

  override def buildSettings: Seq[Def.Setting[?]] = Seq(
    scalaVersion := "2.13.14",
    scalacOptions ++= List(
      "-Yrangepos",
      "-Ywarn-unused"
    ),
    semanticdbEnabled := true,
    semanticdbVersion := "4.9.9"
  )

  override def trigger = allRequirements

  object autoImport {

    val ScalaMeta  = "org.scalameta"    %% "scalameta"   % "4.12.5"
    val Scalactic  = "org.scalactic"    %% "scalactic"   % "3.2.19"
    val Scopt      = "com.github.scopt" %% "scopt"       % "4.1.0"
    val Zio        = "dev.zio"          %% "zio"         % "2.1.14"
    val ZioLogging = "dev.zio"          %% "zio-logging" % "2.4.0"

    val ZioTest         = "dev.zio" %% "zio-test"          % "2.1.14"
    val ZioTestSbt      = "dev.zio" %% "zio-test-sbt"      % "2.1.14"
    val ZioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % "2.1.14"

    val dmtscalaDependencies: Seq[ModuleID] = Seq(ScalaMeta, Scalactic, Scopt, Zio, ZioLogging, ZioTest, ZioTestSbt, ZioTestMagnolia)
  }
}
