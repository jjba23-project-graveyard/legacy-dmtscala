# dmtscala is the Data Model Translator for Scala

# Copyright © 2025 "Josep Bigorra" known as "jjba23" <jjbigorra@gmail.com>

# dmtscala is free software: you can redistribute it and/or modify
# it under the terms of the Lesser GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# dmtscala is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

# You should have received a copy of the Lesser GNU General Public License
# along with dmtscala.  If not, see <https://www.gnu.org/licenses/>.

clean:
	rm -rf .bloop .metals project/target project/project target dmtscala/target
	sbt "bloopInstall"
validate:
	sbt "scalafixAll; scalafmt; test"
fmt:
	sbt "scalafixAll; scalafmt"
test:
	sbt "test"

run-basic-scala:
	sbt "dmtscala/run --to-typescript=true \
		--to-python=true --to-haskell=true \
		--with-inputs=./resources/scala-files/Basic.scala"

run-basic-scala-silent-ts:
	sbt "dmtscala/run --to-typescript=true --silent=true\
		--with-inputs=./resources/scala-files/Basic.scala"

run-advanced-scala:
	sbt "dmtscala/run --to-typescript=true \
		--to-python=true --to-haskell=true \
		--with-inputs=./resources/scala-files/Advanced.scala"
