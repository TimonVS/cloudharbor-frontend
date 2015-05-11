package models.DigitalOcean

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * Created by Rudie on 4-5-2015.
 */
case class Region(
                   slug: String,
                   name: String,
                   sizes: List[String],
                   available: Boolean)

object Region {
  implicit val regionReads: Reads[Region] = (
    (__ \ "slug").read[String]
      and (__ \ "name").read[String]
      and (__ \ "sizes").read[List[String]]
      and (__ \ "available").read[Boolean]
    )(Region.apply _)
}
