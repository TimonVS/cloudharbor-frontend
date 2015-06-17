package controllers.docker

import actors.NotificationActor.{Container, Error}
import controllers.Secured
import models.Notifications.{ContainerNotification, ErrorNotification}
import play.api.Play.current
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.Json
import play.api.libs.ws.WS
import utils.WsUtils

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Rudie on 8-6-2015.
 */
object DeployManagement extends DockerManagement with WsUtils with Secured {

  val DEPLOY_MANAGEMENT = "Deploy Management"

  def deploy(serverUrl: String, imageName: String, imageTag: Option[String], imageRepo: Option[String]) = withAuth { implicit user => implicit request => {
    val url = s"http://$managementUrl/images/$imageName" +
      imageRepo.map(repo => s"&repo=$repo").getOrElse("") +
      imageTag.map(tag => s"&tag=$tag").getOrElse("")

    WS.url(url).withHeaders(dockerInfo(serverUrl)).withMethod("POST").stream().flatMap {
      case (response, enumerator) => enumerator |>>> Iteratee.skipToEof
    }.flatMap { _ =>
      WS.url(s"http://$managementUrl/containers/create").withHeaders(dockerInfo(serverUrl)).post(request.body.asJson.get)
    }.map { result => result.status match {
      case OK => notificationActor ! Container(user.id, ContainerNotification((result.json \ "Id").as[String], "Container is created"))
      case _ => notificationActor ! Error(user.id, ErrorNotification("Error while creating container"))
    }
    }

    Ok(Json.obj("success" -> "Container is being created"))
  }
  }
}
