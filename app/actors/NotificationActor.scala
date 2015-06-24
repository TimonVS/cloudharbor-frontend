package actors

import actors.NotificationActor._
import akka.actor.{Actor, ActorLogging, Props}
import controllers.Notifications
import models.Notifications._
import play.api.libs.json.Json

/**
 * Actor that stores the received notifications in a database
 * and forwards them tho the [[Notifications]] broadcast channel.
 *
 * Created by Rudie de Smit.
 */
class NotificationActor extends Actor with ActorLogging{

  override def receive = {
    case Server(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message), NotificationType.Server))
    case Container(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message), NotificationType.Container))
    case Image(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message), NotificationType.Image))
    case Error(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message), NotificationType.Error))
  }

  /** forwards [[DBNotification]] to the [[Notifications]] broadcast channel */
  private def pushNotification(dbNotification: DBNotification) = {
    Notifications.notificationsIn.push(Notification(dbNotification.id, dbNotification.userId, Json.parse(dbNotification.message), dbNotification.notificationType))
  }
}

object NotificationActor{

  def props: Props = Props[NotificationActor]

  trait NotificationActorMessages

  /** Message for server related notifications */
  case class Server(userId: Int, message: ServerNotification) extends NotificationActorMessages

  /** Message for container related notifications */
  case class Container(userId: Int, message: ContainerNotification) extends NotificationActorMessages

  /** Message for image related notifications */
  case class Image(userId: Int, message: ImageNotification) extends NotificationActorMessages

  /** Message for error related notifications */
  case class Error(userId: Int, message: ErrorNotification) extends NotificationActorMessages
}
