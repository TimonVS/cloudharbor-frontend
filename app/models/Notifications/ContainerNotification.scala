package models.Notifications

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Created by Rudie on 10-6-2015.
 */
case class ContainerNotification(containerId: String, body: String, status: String)

object ContainerNotification {
  implicit val containerNotificationWrites: Writes[ContainerNotification] = (
    (__ \ "id").write[String] and
      (__ \ "body").write[String] and
      (__ \ "status").write[String]
    )(unlift(ContainerNotification.unapply))
}
