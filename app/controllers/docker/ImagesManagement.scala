package controllers.docker

import java.net.ConnectException

import controllers.{Notifications, Secured}
import play.api.Play._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.libs.ws.WS
import utils.WsUtils

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Rudie on 4-6-2015.
 */
object ImagesManagement extends DockerManagement with Secured with WsUtils {

  val IMAGES_MANAGEMENT = "Images Management"

  def createImage(serverUrl: String, imageName: String) = withAuthAsync { implicit user => implicit request =>
    def ok(enumerator: Enumerator[Array[Byte]]) = {
      enumerator.map(new String(_)) |>>> Iteratee.foreach(Notifications.notificationsIn.push)
      Ok("Image is being created")
    }

    WS.url(s"http://$managementUrl/images/$imageName")
      .withHeaders(dockerInfo(serverUrl))
      .withMethod("POST")
      .stream()
      .map { case (response, enumerator) => response.status match {
      case OK => ok(enumerator)
      case _ => InternalServerError(unexpectedError)
    }
    } recover {
      case _: ConnectException => InternalServerError(unavailableJsonMessage(IMAGES_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }
  }
}
