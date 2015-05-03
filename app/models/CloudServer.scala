package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

case class CloudServer(
                    id: BigDecimal,
                    name: String,
                    memory: Int,
                    vcpus: Int,
                    disk: Int,
                    locked: Boolean,
                    status: String,
                    imageId: BigDecimal,
                    imageName: String,
                    regionName: String,
                    ipAddress: Option[String],
                    operatingSystem: String
                    )

object CloudServer{
  implicit val personReads: Reads[CloudServer] = (
    (__ \  "id").read[BigDecimal]
      and (__ \ "name").read[String]
      and (__ \ "memory").read[Int]
      and (__ \ "vcpus").read[Int]
      and (__ \ "disk").read[Int]
      and (__ \ "locked").read[Boolean]
      and (__ \ "status").read[String]
      and (__ \ "image" \ "id").read[BigDecimal]
      and (__ \ "image" \ "name").read[String]
      and (__ \ "region" \ "name").read[String]
      and ((__ \ "networks" \ "v4")(0) \ "ip_address").readNullable[String]
      and (__ \ "image" \ "distribution").read[String]
    )(CloudServer.apply _)
}
