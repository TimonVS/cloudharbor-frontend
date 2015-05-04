package models.DigitalOcean

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * Created by Rudie on 4-5-2015.
 */
case class Network(
                    ipAddress: String,
                    netmask: String,
                    gateway: String,
                    networkType: String)

object Network {
  implicit val readNetwork: Reads[Network] = (
    (__ \ "ip_address").read[String]
      and (__ \ "netmask").read[String]
      and (__ \ "gateway").read[String]
      and (__ \ "type").read[String]
    )(Network.apply _)
}
