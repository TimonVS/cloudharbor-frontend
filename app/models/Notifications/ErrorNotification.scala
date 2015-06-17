package models.Notifications

import play.api.libs.json.{Json, Reads}

/**
 * Created by Rudie on 17-6-2015.
 */
case class ErrorNotification(message: String)

object ErrorNotification {
  implicit val writeErrorNotification: Reads[ErrorNotification] = Json.reads[ErrorNotification]
}
