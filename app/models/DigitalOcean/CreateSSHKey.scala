package models.DigitalOcean

import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._
import play.api.libs.json._

/**
 * Created by Rudie on 3-5-2015.
 */
case class CreateSSHKey(name: String, publicKey: String)

object CreateSSHKey {
  implicit val writeCreateSSHKey = (
    (__ \ "name").write[String]
      and (__ \ "public_key").write[String]
    )(unlift(CreateSSHKey.unapply))
}
