package actors

import actors.NotificationActor._
import akka.actor.{Actor, ActorLogging, Props}
import controllers.Notifications
import models.Notifications._
import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by ThomasWorkBook on 02/05/15.
 */
class NotificationActor extends Actor with ActorLogging{

  def receive = {
    case CreateServerNotification(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message).toString, DateTime.now, NotificationType.ServerCreate.toString))
    case DeleteServerNotification(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message).toString, DateTime.now, NotificationType.ServerDelete.toString))
    case CreateContainerNotification(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message).toString, DateTime.now, NotificationType.ContainerCreate.toString))
    case DeleteServerNotification(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message).toString, DateTime.now, NotificationType.ContainerDelete.toString))
    case CreateImageNotification(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message).toString, DateTime.now, NotificationType.ImageCreate.toString))
    case DeleteImageNotification(userId, message) =>
      pushNotification(DBNotification.create(userId, Json.toJson(message).toString, DateTime.now, NotificationType.ImageDelete.toString))
  }

  def pushNotification(dbNotification: DBNotification) = {
    Notifications.notificationsIn.push(Notification(dbNotification.id, dbNotification.userId, Json.parse(dbNotification.message), dbNotification.notificationType))
  }

}

object NotificationActor{

  def props: Props = Props[NotificationActor]

  trait NotificationActorMessages

  case class CreateServerNotification(userId: Int, message: ServerNotification) extends NotificationActorMessages

  case class DeleteServerNotification(userId: Int, message: ServerNotification) extends NotificationActorMessages

  case class CreateContainerNotification(userId: Int, message: ContainerNotification) extends NotificationActorMessages

  case class DeleteContainerNotification(userId: Int, message: ContainerNotification) extends NotificationActorMessages

  case class CreateImageNotification(userId: Int, message: ImageNotification) extends NotificationActorMessages

  case class DeleteImageNotification(userId: Int, message: ImageNotification) extends NotificationActorMessages
}
