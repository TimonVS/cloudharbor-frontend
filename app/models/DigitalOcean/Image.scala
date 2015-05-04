package models.DigitalOcean

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * Created by Rudie on 4-5-2015.
 */
case class Image(
                  id: BigDecimal,
                  name: String,
                  imageType: String,
                  distribution: String,
                  slug: Option[String],
                  public: Boolean,
                  regions: List[String],
                  minDiskSize: BigDecimal)

object Image {
  implicit val imageReads: Reads[Image] = (
    (__ \ "id").read[BigDecimal]
      and (__ \ "name").read[String]
      and (__ \ "type").read[String]
      and (__ \ "distribution").read[String]
      and (__ \ "slug").read[Option[String]]
      and (__ \ "public").read[Boolean]
      and (__ \ "regions").read[List[String]]
      and (__ \ "min_disk_size").read[BigDecimal]
    )(Image.apply _)
}
