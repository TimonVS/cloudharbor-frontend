package actors

import actors.NotificationActor._
import akka.actor.{Actor, ActorLogging, Props}
import controllers.Notifications
import models.Notifications._
import play.api.libs.json.Json

/**
 * Created by ThomasWorkBook on 02/05/15.
 */
class NotificationActor extends Actor with ActorLogging{

  override def receive = {
    case Server(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message), NotificationType.Server))
    case Container(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message), NotificationType.Container))
    case Image(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message), NotificationType.Image))
  }

  def pushNotification(dbNotification: DBNotification) = {
    Notifications.notificationsIn.push(Notification(dbNotification.id, dbNotification.userId, Json.parse(dbNotification.message), dbNotification.notificationType))
  }
}

object NotificationActor{

  def props: Props = Props[NotificationActor]

  trait NotificationActorMessages

  case class Server(userId: Int, message: ServerNotification) extends NotificationActorMessages

  case class Container(userId: Int, message: ContainerNotification) extends NotificationActorMessages

  case class Image(userId: Int, message: ImageNotification) extends NotificationActorMessages
}
