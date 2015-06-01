package controllers

import java.net.ConnectException
import controllers.ServerManagement._
import models.Server
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.{Results, Result, Controller}
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Link Api for containerManagement of docker containers
 * TODO: implement better recovery when the management service is unavailable
 * Implement logging + retry with ping to check if the management service is available
 * TODO: remove hardcoded managementUrl and implement ETCD
 */
object ContainerManagement extends Controller with Secured{

  val managementUrl = current.configuration.getString("management.url").get + ":" + current.configuration.getInt("management.port").get

  val unavailableJsonMessage = Json.obj("error" -> "Sorry, but the Container Management Service is currently not available")
  val unexpectedError = Json.obj("error" -> "Sorry, but an unexpected error occurred")

  def dockerInfo(serverUrl: String): (String, String) = {
    "Docker-Info" -> (s"$serverUrl:4243")
  }

  def sendEmptyPost(url: String, serverUrl: String): Future[Result] =
    WS.url(url)
      .withHeaders(dockerInfo(serverUrl))
      .post(Results.EmptyContent()).map(r => r.status match{
        case NO_CONTENT => Ok(r.json)
        case NOT_MODIFIED => BadRequest(r.json)
        case NOT_FOUND => NotFound(r.json)
        case INTERNAL_SERVER_ERROR => BadRequest(r.json)
    }) recover{
      case _: ConnectException => InternalServerError
    }

  def overview = withAuth { implicit user => implicit request =>
    Ok(views.html.containerManagement.overview())
  }

  def show(serverUrl: String, containerId: String) = TODO

  def pingDockerRemoteApi(serverUrl: String) = withAuthAsync( implicit user => implicit request =>
    WS.url(s"http://$managementUrl/ping")
      .withHeaders(dockerInfo(serverUrl))
      .get()
      .map{r => r.status match {
        case OK => Ok(r.json)
        case _ => BadRequest(r.json)
      }
    } recover{
      case _: ConnectException => InternalServerError(unavailableJsonMessage)
      case _ => InternalServerError(unexpectedError)
    }
  )

  def listContainers(serverUrl: String) = withAuthAsync { implicit user => implicit request =>
    WS.url(s"http://$managementUrl/containers")
      .withHeaders(dockerInfo(serverUrl))
      .get()
      .map(r => Ok(r.json))
      .recover {
        case _: ConnectException => BadRequest(unavailableJsonMessage)
        case _ => InternalServerError(unexpectedError)
      }
  }

  def listContainers(serverUrls: Seq[String]) = play.mvc.Results.TODO

  def startContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/start", serverUrl)
  }

  def stopContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/stop", serverUrl)
  }

  def killContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/kill", serverUrl)
  }

  def pauseContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/pause", serverUrl)
  }

  def unPauseContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/unpause", serverUrl)
  }

  def renameContainer(serverUrl: String, containerId: String, newName: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/rename?newName=$newName", serverUrl)
  }

  def removeContainer(serverUrl: String, containerId: String, deleteVolumes: Boolean, force: Boolean) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/remove?deleteVolumes=$deleteVolumes&force=$force", serverUrl)
  }
}

