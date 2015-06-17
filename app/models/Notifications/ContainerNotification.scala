package models.Notifications

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

/**
 * Created by Rudie on 10-6-2015.
 */
case class ContainerNotification(containerId: String, body: String)

object ContainerNotification {
  implicit val containerNotificationWrites: Writes[ContainerNotification] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "body").write[String]
    )(unlift(ContainerNotification.unapply))
}
