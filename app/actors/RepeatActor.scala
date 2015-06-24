package actors

import actors.RepeatActor.Repeat
import akka.actor.{Actor, ActorLogging, Props}
import play.api.Logger

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success}


/**
 * Actor for repeating a specific action, and executing a method when the action is done.
 *
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

  /**
   * Send this message to the [[RepeatActor]] to repeat a specific action.
   * @param action action to repeat, when the result is false the action will be repeated
   *               and when te result is true the onDone function will be called
   * @param onDone function that will be called when the action returns true
   * @param onError function that will be called when the action fails
   * @param interval interval for repeating the actions, default is 5 sec
   */
  case class Repeat(action: () => Future[Boolean],
                    onDone: () => Unit,
                    onError: Throwable => Unit = error => Logger.trace(error.getMessage, error),
                    interval: FiniteDuration = FiniteDuration(5, "sec"))

}
