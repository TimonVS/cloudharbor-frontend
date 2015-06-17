package models.Notifications

import play.api.libs.json.{Json, Writes}

/**
 * Created by Rudie on 17-6-2015.
 */
case class ErrorNotification(message: String)

object ErrorNotification {
  implicit val writeErrorNotification: Writes[ErrorNotification] = Json.writes[ErrorNotification]
}
