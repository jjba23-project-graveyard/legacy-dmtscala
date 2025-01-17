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

import scala.util.control.NonFatal

ThisBuild / organization := "io.github.jjba23"
name := "dmtscala"

ThisBuild / version := "0.0.1"

ThisBuild / resolvers += "Mulesoft".at(
  "https://repository.mulesoft.org/nexus/content/repositories/public/"
)

ThisBuild / versionScheme := Some("semver-spec")

val dmtscala = (project
  .in(file("./dmtscala")))
  .settings(
    libraryDependencies := dmtscalaDependencies,
  )

ThisBuild / licenses := Seq(
  "LGPL3" -> url("https://www.gnu.org/licenses/lgpl-3.0.en.html")
)

ThisBuild / homepage := Some(url("https://codeberg.org/jjba23/dmtscala"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://codeberg.org/jjba23/dmtscala"),
    "scm:git@codeberg.org:jjba23/dmtscala.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "jjba23",
    name = "Josep Bigorra",
    email = "jjbigorra@gmail.com",
    url = url("https://codeberg.org/jjba23")
  )
)
