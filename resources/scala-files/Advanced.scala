package my.company

import java.time.{LocalTime, ZonedDateTime}

package object IpcoDomain {

  case class Location(
    id: String,
    last_updated: ZonedDateTime,
    `type`: String,
    name: Option[String] = None,
    address: String,
    city: String,
    postal_code: String,
    country: String, // should be 3-letter code
    coordinates: GeoLocation,
    related_locations: Seq[AdditionalGeoLocation] = Nil,
    evses: Seq[Evse] = Nil,
    directions: Seq[DisplayText] = Nil,
    operator: Option[BusinessDetails] = None,
    suboperator: Option[BusinessDetails] = None,
    owner: Option[BusinessDetails] = None,
    facilities: Seq[Facility] = Nil,
    time_zone: Option[String] = None,
    opening_times: Option[Hours] = None,
    charging_when_closed: Option[Boolean] = None,
    images: Seq[Image] = Nil,
    energy_mix: Option[EnergyMix] = None,
  )

  case class LocationPatch(
    last_updated: Option[ZonedDateTime] = None,
    `type`: Option[String] = None,
    name: Option[String] = None,
    address: Option[String] = None,
    city: Option[String] = None,
    postal_code: Option[String] = None,
    country: Option[String] = None,
    coordinates: Option[GeoLocation] = None,
    related_locations: Option[Seq[AdditionalGeoLocation]] = None,
    evses: Option[Seq[Evse]] = None,
    directions: Option[Seq[DisplayText]] = None,
    operator: Option[BusinessDetails] = None,
    suboperator: Option[BusinessDetails] = None,
    owner: Option[BusinessDetails] = None,
    facilities: Option[Seq[Facility]] = None,
    time_zone: Option[String] = None,
    opening_times: Option[Hours] = None,
    charging_when_closed: Option[Boolean] = None,
    images: Option[Seq[Image]] = None,
    energy_mix: Option[EnergyMix] = None,
  )

  case class Facility(name: String)

  case class BusinessDetails(
    name: String,
    logo: Option[Image],
    website: Option[String],
  )

  case class Image(
    url: String,
    category: String,
    `type`: String,
    width: Option[Int] = None,
    height: Option[Int] = None,
    thumbnail: Option[String] = None,
  )

  case class EnergySource(
    source: String,
    percentage: Double,
  )

  case class EnvironmentalImpact(
    source: String,
    percentage: Double,
  )

  case class EnergyMix(
    is_green_energy: Boolean,
    energy_sources: Seq[EnergySource] = Nil,
    environ_impact: Seq[EnvironmentalImpact] = Nil,
    supplier_name: Option[String] = None,
    energy_product_name: Option[String] = None,
  )

  case class RegularHours(
    weekday: Int,
    period_begin: LocalTime,
    period_end: LocalTime,
  )

  case class ExceptionalPeriod(
    period_begin: ZonedDateTime,
    period_end: ZonedDateTime,
  )

  case class Hours(
    twentyfourseven: Boolean,
    regular_hours: Seq[RegularHours] = Nil,
    exceptional_openings: Seq[ExceptionalPeriod] = Nil,
    exceptional_closings: Seq[ExceptionalPeriod] = Nil,
  )

  case class Connector(
    id: String,
    last_updated: ZonedDateTime,
    standard: String,
    format: String,
    power_type: String,
    voltage: Int,
    amperage: Int,
    tariff_id: Option[String],
    terms_and_conditions: Option[String] = None,
  )

  case class ConnectorPatch(
    last_updated: Option[ZonedDateTime] = None,
    standard: Option[String] = None,
    format: Option[String] = None,
    power_type: Option[String] = None,
    voltage: Option[Int] = None,
    amperage: Option[Int] = None,
    tariffId: Option[String] = None,
    terms_and_conditions: Option[String] = None,
  )

  /** Electric Vehicle Supply Equipment
    */
  case class Evse(
    uid: String,
    last_updated: ZonedDateTime,
    status: AvailabilityStatus,
    connectors: Seq[Connector],
    status_schedule: Seq[StatusSchedule] = Nil,
    capabilities: Seq[String] = Nil,
    evse_id: Option[String] = None,
    floor_level: Option[String] = None,
    coordinates: Option[GeoLocation] = None,
    physical_reference: Option[String] = None,
    directions: Seq[DisplayText] = Nil,
    parking_restrictions: Seq[String] = Nil,
    images: Seq[Image] = Nil,
  )

  case class PpcoConnectorStatus(
    status: AvailabilityStatus,
  )

  case class EvsePatch(
    last_pdated: Option[ZonedDateTime] = None,
    status: Option[String] = None,
    connectors: Option[Seq[Connector]] = None,
    status_schedule: Option[Seq[StatusSchedule]] = None,
    capabilities: Option[Seq[String]] = None,
    evse_id: Option[String] = None,
    floor_level: Option[String] = None,
    coordinates: Option[GeoLocation] = None,
    physical_reference: Option[String] = None,
    directions: Option[Seq[DisplayText]] = None,
    parking_restrictions: Option[Seq[String]] = None,
    images: Option[Seq[Image]] = None,
  )

  case class AdditionalGeoLocation(
    latitude: String,
    longitude: String,
    name: Option[DisplayText] = None,
  )

  case class GeoLocation(
    latitude: String,
    longitude: String,
  )

  case class StatusSchedule(
    period_begin: ZonedDateTime,
    period_end: Option[ZonedDateTime],
    status: String,
  )

  case class DisplayText(
    language: String,
    text: String,
  )

  case class IpcoResponse(
    status_code: Int,
    status_message: Option[String],
    timestamp: String,
  )

  case class CdrDimension(
    `type`: Option[String],
    volume: Option[Int],
  )

  case class ChargingPeriod(
    start_date_time: Option[String],
    dimensions: Option[Seq[CdrDimension]],
  )

  case class IpcoTariff(
    id: String,
    currency: String,
    elements: Seq[Element],
    last_updated: String,
  )

  case class ChargingDetailRecord(
    id: String,
    start_date_time: ZonedDateTime,
    stop_date_time: ZonedDateTime,
    auth_id: String,
    visual_number: String,
    uid: String,
    auth_method: AuthMethod,
    location: Location,
    meter_id: Option[String],
    currency: String,
    tariff: IpcoTariff,
    charging_periods: Option[Seq[ChargingPeriod]],
    total_cost: Double,
    total_energy: Double,
    total_time: Double,
    total_parking_time: Option[Double],
    remark: Option[String],
    last_updated: ZonedDateTime,
  )

}
