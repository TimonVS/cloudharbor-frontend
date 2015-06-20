package controllers.docker

import utils.Secured

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Link Api for containerManagement of docker containers
 * TODO: implement better recovery when the management service is unavailable
 * Implement logging + retry with ping to check if the management service is available
 * TODO: remove hardcoded managementUrl and implement ETCD
 */
object ContainerManagement extends DockerManagement with Secured {

  val CONTAINER_MANAGEMENT = "Container Management"

  def show(serverUrl: String, containerId: String) =
    forwardGetWithAuth(s"http://$managementUrl/containers/$containerId", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)

  def listContainers(serverUrl: String) =
    forwardGetWithAuth(s"http://$managementUrl/containers", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)

  def listContainers(serverUrls: Seq[String]) = play.mvc.Results.TODO

  def startContainer(serverUrl: String, containerId: String) =
    forwardPostWithAuth(s"http://$managementUrl/containers/$containerId/start", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)

  def stopContainer(serverUrl: String, containerId: String) =
    forwardPostWithAuth(s"http://$managementUrl/containers/$containerId/stop", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)

  def killContainer(serverUrl: String, containerId: String) =
    forwardPostWithAuth(s"http://$managementUrl/containers/$containerId/kill", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)

  def pauseContainer(serverUrl: String, containerId: String) =
    forwardPostWithAuth(s"http://$managementUrl/containers/$containerId/pause", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)

  def unPauseContainer(serverUrl: String, containerId: String) =
    forwardPostWithAuth(s"http://$managementUrl/containers/$containerId/unpause", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)

  def renameContainer(serverUrl: String, containerId: String, newName: String) =
    forwardPostWithAuth(s"http://$managementUrl/containers/$containerId/rename?newName=$newName", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)

  def removeContainer(serverUrl: String, containerId: String, deleteVolumes: Boolean, force: Boolean) =
    forwardPostWithAuth(s"http://$managementUrl/containers/$containerId/remove?deleteVolumes=$deleteVolumes&force=$force", serverUrl, dockerInfo, CONTAINER_MANAGEMENT)
}

