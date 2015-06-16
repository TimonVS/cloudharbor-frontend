package models.Notifications

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

/**
 * Created by Rudie on 10-6-2015.
 */
case class ServerNotification(serverId: String, body: String)

object ServerNotification {
  implicit val serverNotificationWrites: Writes[ServerNotification] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "body").write[String]
    )(unlift(ServerNotification.unapply))
}
