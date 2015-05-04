package models.DigitalOcean

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * Created by Rudie on 4-5-2015.
 */
case class Networks(ipv4: List[Network], ipv6: List[Network])

object Networks {
  implicit val readNetwork: Reads[Networks] = (
    (__ \ "v4").read[List[Network]]
      and (__ \ "v6").read[List[Network]]
    )(Networks.apply _)
}
