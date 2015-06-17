package controllers.docker

import java.net.ConnectException

import actors.NotificationActor.Image
import controllers.Secured
import models.Notifications.ImageNotification
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

  def listImages(serverUrl: String) = withAuthAsync { implicit user => implicit request =>
    forwardGet(s"http://$managementUrl/images", serverUrl, dockerInfo, IMAGES_MANAGEMENT)
  }

  def inspectImage(serverUrl: String, imageName: String) = withAuthAsync { implicit user => implicit request =>
    forwardGet(s"http://$managementUrl/images/$imageName", serverUrl, dockerInfo, IMAGES_MANAGEMENT)
  }

  def historyImage(serverUrl: String, imageName: String) = withAuthAsync { implicit user => implicit request =>
    forwardGet(s"http://$managementUrl/images/$imageName/history", serverUrl, dockerInfo, IMAGES_MANAGEMENT)
  }

  def removeImage(serverUrl: String, imageName: String) = withAuthAsync { implicit user => implicit request =>
    forwardDelete(s"http://$managementUrl/images/$imageName", serverUrl, dockerInfo, IMAGES_MANAGEMENT)
  }

  def createImage(serverUrl: String, imageName: String) = withAuthAsync { implicit user => implicit request =>
    def ok(enumerator: Enumerator[Array[Byte]]) = {
      (enumerator |>>> Iteratee.skipToEof).foreach(_ => notificationActor ! Image(user.id, ImageNotification(imageName, "")))
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

  def pushImage(serverUrl: String, repositoryName: String, imageName: String) = withAuthAsync { implicit user => implicit request =>
    def ok(enumerator: Enumerator[Array[Byte]]) = {
      Ok("Image is being pushed")
    }

    WS.url(s"http://$managementUrl/images/$imageName/push/$repositoryName")
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
