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

package basic

import java.util.UUID
import java.time.Instant

case class MonitoringSiteRegistrationDetails(
  monitoringSiteId: Int,
  solarEdgeSiteId: UUID
)

case class DerDetailsDto(
  id: UUID,
  `type`: String,
  serial: String,
  vendor: String,
  derControllerId: UUID
)

case class DerDetailsResponseDto(
  ders: List[DerDetailsDto]
)

case class SolarEdgeOnboardingFailureDto(
  id: UUID,
  monitoringSiteId: String,
  monitoringSiteName: String,
  derSerial: String,
  creationTime: Instant,
  commentsCount: Int,
  createdBy: String,
  taskStatus: String,
  errorDescription: String,
  lastFailureStageMessage: Option[String],
  lastFailureStageDate: Option[Instant]
)
