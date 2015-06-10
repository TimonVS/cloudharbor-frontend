package models.Notifications

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

/**
 * Created by Rudie on 10-6-2015.
 */
case class ImageNotification(imageName: String, body: String)

object ImageNotification {
  implicit val imageNotificationWrites: Writes[ImageNotification] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "body").write[String]
    )(unlift(ImageNotification.unapply))
}
