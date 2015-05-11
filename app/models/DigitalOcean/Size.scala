package models.DigitalOcean

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * Created by Rudie on 4-5-2015.
 */
case class Size(
                 slug: String,
                 available: Boolean,
                 transfer: BigDecimal,
                 priceMonthly: BigDecimal,
                 priceHourly: BigDecimal,
                 memory: BigDecimal,
                 vcpus: BigDecimal,
                 disk: BigDecimal,
                 regions: List[String])

object Size {
  implicit val readSize: Reads[Size] = (
    (__ \ "slug").read[String]
      and (__ \ "available").read[Boolean]
      and (__ \ "transfer").read[BigDecimal]
      and (__ \ "price_monthly").read[BigDecimal]
      and (__ \ "price_hourly").read[BigDecimal]
      and (__ \ "memory").read[BigDecimal]
      and (__ \ "vcpus").read[BigDecimal]
      and (__ \ "disk").read[BigDecimal]
      and (__ \ "regions").read[List[String]]
    )(Size.apply _)
}
