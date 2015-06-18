package models.Notifications

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Created by Rudie on 10-6-2015.
 */
case class ServerNotification(serverId: String, body: String, status: String)

object ServerNotification {
  implicit val serverNotificationWrites: Writes[ServerNotification] = (
    (__ \ "id").write[String] and
      (__ \ "body").write[String] and
      (__ \ "status").write[String]
    )(unlift(ServerNotification.unapply))
}
