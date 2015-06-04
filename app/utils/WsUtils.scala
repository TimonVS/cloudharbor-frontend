package utils

import java.net.ConnectException
import play.api.libs.json.Json
import play.api.libs.ws.{WS, WSResponse}
import play.api.mvc.{Results, Result}
import play.api.Play.current
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by ThomasWorkBook on 04/06/15.
 */
trait WsUtils {

  def unavailableJsonMessage(service: String) = Json.obj("error" -> s"Sorry, but the $service is currently not available")

  val unexpectedError = Json.obj("error" -> "Sorry, but an unexpected error occurred")

  def forwardResponse(response: WSResponse): Result = Results.Status(response.status)(response.body)

  def sendEmptyPost(url: String, headerContent: String, createHeader: String => (String, String), service: String): Future[Result] =
    WS.url(url)
      .withHeaders(createHeader(headerContent))
      .post(Results.EmptyContent()).map(forwardResponse(_))
      .recover{
        case _: ConnectException => Results.InternalServerError(unavailableJsonMessage(service))
        case _ => Results.InternalServerError(unexpectedError)
      }
}
