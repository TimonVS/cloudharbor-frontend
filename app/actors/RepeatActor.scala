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
    case message@Repeat(action, done, interval) =>
      action().onComplete {
        case Success(result) => if (result) done() else system.scheduler.scheduleOnce(interval, self, message)
        case Failure(error) => Logger.trace(error.getMessage, error)
      }
  }
}


object RepeatActor {

  def props: Props = Props[RepeatActor]

  case class Repeat(action: () => Future[Boolean], done: () => Unit, interval: FiniteDuration = FiniteDuration(5, "sec"))

}
