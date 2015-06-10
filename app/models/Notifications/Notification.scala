package models.Notifications

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, JsValue, Writes}

/**
 * Created by Rudie on 10-6-2015.
 */
case class Notification(id: Int, userId: Int, message: JsValue, notificationType: String)

object Notification {
  implicit val notificationWrites: Writes[Notification] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "userId").write[Int] and
      (JsPath \ "message").write[JsValue] and
      (JsPath \ "type").write[String]
    )(unlift(Notification.unapply))
}
