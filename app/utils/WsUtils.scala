package utils

import java.net.ConnectException

import actors.NotificationActor
import actors.NotificationActor.NotificationActorMessages
import play.api.Play.current
import play.api.http.Status._
import play.api.libs.concurrent.Akka
import play.api.libs.json.Json
import play.api.libs.ws.{WS, WSResponse}
import play.api.mvc.{Result, Results}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by ThomasWorkBook on 04/06/15.
 */
trait WsUtils {

  lazy val notificationActor = Akka.system.actorOf(NotificationActor.props, name = "notificationActor")

  val unexpectedError = Json.obj("error" -> "Sorry, but an unexpected error occurred")

  def optToUrlParam[T](param: String, opt: Option[T]) = opt.map(opt => s"&$param=${opt.toString}").getOrElse("")

  /**
   * Forwards a HTTP GET request
   * Created by Rudie de Smit
   */
  def forwardGet[T](url: String, headerContent: T, createHeader: T => (String, String), service: String) =
    WS.url(url)
      .withHeaders(createHeader(headerContent))
      .get()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => Results.InternalServerError(unavailableJsonMessage(service))
      case _ => Results.InternalServerError(unexpectedError)
    }

  /** Created by Rudie de Smit */
  def forwardResponseWithNotification(response: WSResponse, notification: => NotificationActorMessages): Result = {
    if (response.status == OK) notificationActor ! notification
    forwardResponse(response)
  }

  /**
   * Forwards a HTTP POST request
   * Created by Rudie de Smit
   */
  def forwardPost[T](url: String, headerContent: T, createHeader: T => (String, String), service: String) =
    WS.url(url)
      .withHeaders(createHeader(headerContent))
      .post(Results.EmptyContent())
      .map(forwardResponse)
      .recover {
        case _: ConnectException => Results.InternalServerError(unavailableJsonMessage(service))
        case _ => Results.InternalServerError(unexpectedError)
    }

  /**
   * Forwards a HTTP DELETE request
   * Created by Rudie de Smit
   */
  def forwardDelete[T](url: String, headerContent: T, createHeader: T => (String, String), service: String) =
    WS.url(url)
      .withHeaders(createHeader(headerContent))
      .delete()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => Results.InternalServerError(unavailableJsonMessage(service))
      case _ => Results.InternalServerError(unexpectedError)
    }

  def unavailableJsonMessage(service: String) = Json.obj("error" -> s"Sorry, but the $service is currently not available")

  def forwardResponse(response: WSResponse): Result =
    Results.Status(response.status)(response.body)
}
