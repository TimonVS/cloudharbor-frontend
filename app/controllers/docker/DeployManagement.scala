package controllers.docker

import actors.NotificationActor.{Container, Error}
import models.Notifications.{ContainerNotification, ErrorNotification}
import play.api.Play.current
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.Json
import play.api.libs.ws.WS
import utils.Secured

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Api-gateway for deploy related calls.
 *
 * Created by Rudie on 8-6-2015.
 */
object DeployManagement extends DockerManagement with Secured {

  val DEPLOY_MANAGEMENT = "Deploy Management"

  def deploy(serverUrl: String, imageName: String, imageTag: Option[String], imageRepo: Option[String]) = withAuth { implicit user => implicit request => {
    val url = s"http://$managementUrl/images/$imageName?${optToUrlParam("repo", imageRepo)}${optToUrlParam("tag", imageTag)}"

    WS.url(url).withHeaders(dockerInfo(serverUrl)).withMethod("POST").stream().flatMap {
      case (response, enumerator) => enumerator |>>> Iteratee.skipToEof
    }.flatMap { _ =>
      WS.url(s"http://$managementUrl/containers/create").withHeaders(dockerInfo(serverUrl)).post(request.body.asJson.get)
    }.map { result => result.status match {
      case OK => notificationActor ! Container(user.id, ContainerNotification((result.json \ "Id").as[String], "Container is created", "active"))
      case _ => notificationActor ! Error(user.id, ErrorNotification("Error while creating container"))
    }
    }

    Ok(Json.obj("success" -> "Container is being created"))
  }
  }
}
