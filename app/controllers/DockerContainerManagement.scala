package controllers

import java.net.ConnectException

import play.api.Play.current
import play.api.libs.ws.WS
import utils.WsUtils

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Link Api for containerManagement of docker containers
 * TODO: implement better recovery when the management service is unavailable
 * Implement logging + retry with ping to check if the management service is available
 * TODO: remove hardcoded managementUrl and implement ETCD
 */
object DockerContainerManagement extends DockerManagement with Secured with WsUtils {

  val CONTAINER_MANAGEMENT = "Container Management"

  def overview = withAuth { implicit user => implicit request =>
    Ok(views.html.containerManagement.overview())
  }

  def show(serverUrl: String, containerId: String) = TODO

  def pingDockerRemoteApi(serverUrl: String) = withAuthAsync( implicit user => implicit request =>
    WS.url(s"http://$managementUrl/ping")
      .withHeaders(dockerInfo(serverUrl))
      .get()
      .map(forwardResponse(_))
      .recover{
        case _: ConnectException => InternalServerError(unavailableJsonMessage(CONTAINER_MANAGEMENT))
        case _ => InternalServerError(unexpectedError)
      }
  )

  def listContainers(serverUrl: String) = withAuthAsync { implicit user => implicit request =>
    WS.url(s"http://$managementUrl/containers")
      .withHeaders(dockerInfo(serverUrl))
      .get()
      .map(forwardResponse(_))
      .recover {
        case _: ConnectException => BadRequest(unavailableJsonMessage(CONTAINER_MANAGEMENT))
        case _ => InternalServerError(unexpectedError)
      }
  }

  def listContainers(serverUrls: Seq[String]) = play.mvc.Results.TODO

  def startContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/start", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)
  }

  def stopContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/stop", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)
  }

  def killContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/kill", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)
  }

  def pauseContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/pause", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)
  }

  def unPauseContainer(serverUrl: String, containerId: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/unpause", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)
  }

  def renameContainer(serverUrl: String, containerId: String, newName: String) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/rename?newName=$newName", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)
  }

  def removeContainer(serverUrl: String, containerId: String, deleteVolumes: Boolean, force: Boolean) = withAuthAsync { implicit user => implicit request =>
    sendEmptyPost(s"http://$managementUrl/containers/$containerId/remove?deleteVolumes=$deleteVolumes&force=$force", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)
  }
}

