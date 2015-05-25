package models.DigitalOcean

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class Droplet(
                    id: BigDecimal,
                    name: String,
                    memory: Int,
                    vcpus: Int,
                    disk: Int,
                    locked: Boolean,
                    status: String,
                    createdAt: String,
                    image: Image,
                    size: Size,
                    networks: Networks,
                    region: Region
                    ){

  def getIpAddresses: List[String] = this.networks.ipv4.map(_.ipAddress) ++ this.networks.ipv6.map(_.ipAddress)

}

object Droplet {
  implicit val dropletReads: Reads[Droplet] = (
    (__ \  "id").read[BigDecimal]
      and (__ \ "name").read[String]
      and (__ \ "memory").read[Int]
      and (__ \ "vcpus").read[Int]
      and (__ \ "disk").read[Int]
      and (__ \ "locked").read[Boolean]
      and (__ \ "status").read[String]
      and (__ \ "created_at").read[String]
      and (__ \ "image").read[Image]
      and (__ \ "size").read[Size]
      and (__ \ "networks").read[Networks]
      and (__ \ "region").read[Region]
    )(Droplet.apply _)
}
