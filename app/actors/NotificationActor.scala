package actors

import actors.NotificationActor.CreateServerNotification
import akka.actor.{ActorLogging, Actor}
import models.Notification
import models.NotificationType.NotificationType
import org.joda.time.DateTime

/**
 * Created by ThomasWorkBook on 02/05/15.
 */
class NotificationActor extends Actor with ActorLogging{

  def receive = {
    case CreateServerNotification(userId, message, notificationType) => {
      Notification.create(userId, message, DateTime.now(), notificationType.toString)
    }
  }

}

object NotificationActor{
  trait NotificationActorMessages
  case class CreateServerNotification(userId: Int, message: String, notificationType: NotificationType) extends NotificationActorMessages
  case class DeleteServerNotification(userId: Int, message: String, notificationType: NotificationType) extends NotificationActorMessages
}
