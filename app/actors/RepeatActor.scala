package actors

import actors.RepeatActor.Repeat
import akka.actor.{Actor, ActorLogging, Props}
import play.api.Logger

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success}


/**
 * Created by Rudie on 11-6-2015.
 */
class RepeatActor extends Actor with ActorLogging {

  import context._

  override def receive = {
    case message@Repeat(action, onDone, onError, interval) =>
      action().onComplete {
        case Success(result) => if (result) onDone() else system.scheduler.scheduleOnce(interval, self, message)
        case Failure(error) => onError(error)
      }
  }
}


object RepeatActor {

  def props: Props = Props[RepeatActor]

  case class Repeat(action: () => Future[Boolean],
                    onDone: () => Unit,
                    onError: Throwable => Unit = error => Logger.trace(error.getMessage, error),
                    interval: FiniteDuration = FiniteDuration(5, "sec"))

}
