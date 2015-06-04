package controllers

import java.net.ConnectException

import controllers.DockerContainerManagement._
import play.api.Play._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Rudie on 4-6-2015.
 */
object DockerImagesManagement extends DockerManagement with Secured {

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
      case _: ConnectException => InternalServerError(unavailableJsonMessage(CONTAINER_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }
  }
}
