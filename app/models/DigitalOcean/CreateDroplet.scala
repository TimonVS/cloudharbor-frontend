package models.DigitalOcean

import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._
import play.api.libs.json._

/**
 * Created by Rudie on 3-5-2015.
 */
case class CreateDroplet(
                          name: String,
                          image: String,
                          region: String,
                          size: String,
                          backups: Boolean,
                          ipv6: Boolean,
                          sshKeys: Option[List[BigDecimal]])

object CreateDroplet {
  implicit val writeCreateDroplet = (
    (__ \ "name").write[String]
      and (__ \ "image").write[String]
      and (__ \ "region").write[String]
      and (__ \ "size").write[String]
      and (__ \ "backups").write[Boolean]
      and (__ \ "ipv6").write[Boolean]
      and (__ \ "ssh_keys").writeNullable[List[BigDecimal]]
    )(unlift(CreateDroplet.unapply))
}