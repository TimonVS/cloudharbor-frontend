package controllers.docker

import java.net.ConnectException

import actors.NotificationActor.{Error, Image}
import models.Notifications.{ErrorNotification, ImageNotification}
import play.api.Play._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.libs.json.Json
import play.api.libs.ws.WS
import utils.Secured

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Rudie on 4-6-2015.
 */
object ImagesManagement extends DockerManagement with Secured {

  val IMAGES_MANAGEMENT = "Images Management"

  def listImages(serverUrl: String) =
    forwardGetWithAuth(s"http://$managementUrl/images", serverUrl, dockerInfo, IMAGES_MANAGEMENT)

  def inspectImage(serverUrl: String, imageName: String) =
    forwardGetWithAuth(s"http://$managementUrl/images/$imageName", serverUrl, dockerInfo, IMAGES_MANAGEMENT)

  def historyImage(serverUrl: String, imageName: String) =
    forwardGetWithAuth(s"http://$managementUrl/images/$imageName/history", serverUrl, dockerInfo, IMAGES_MANAGEMENT)

  def removeImage(serverUrl: String, imageName: String) =
    forwardDeleteWithAuth(s"http://$managementUrl/images/$imageName", serverUrl, dockerInfo, IMAGES_MANAGEMENT)

  def createImage(serverUrl: String, imageName: String, repo: Option[String], tag: Option[String]) = withAuthAsync { implicit user => implicit request =>
    val url = s"http://$managementUrl/images/$imageName?${optToUrlParam("repo", repo)}${optToUrlParam("tag", tag)}"

    def ok(enumerator: Enumerator[Array[Byte]]) = {
      (enumerator.map(new String(_)) |>>> Iteratee.fold(true){(r, c) =>
        r & !c.contains("error")})
        .foreach { success =>
        if (success) notificationActor ! Image(user.id, ImageNotification(imageName, "Image is created"))
        else notificationActor ! Error(user.id, ErrorNotification("Image could not be created"))
      }

      Ok(Json.obj("success" -> "Image is being created"))
    }

    WS.url(url)
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
      Ok(Json.obj("success" -> "Image is being pushed"))
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
