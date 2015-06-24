package controllers

import java.net.ConnectException

import actors.NotificationActor.Error
import models.Notifications.ErrorNotification
import models._
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.{Controller, Results}
import utils.{Secured, ServerManagementNotifications, WsUtils}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Controller object for every Server related action
 */
object ServerManagement extends Controller with Secured with WsUtils with ServerManagementNotifications {

  val serverManagementUrl = current.configuration.getString("serverManagement.url").get + ":" + current.configuration.getInt("serverManagement.port").get
  val SERVER_MANAGEMENT = "Server Management"

  val messenger: Messenger = HttpMessenger


  def ping = withAuthAsync { implicit user => implicit request =>
    messenger.ping
  }

  /**
   * Ajax get request for every server a user has at digital ocean
   */
  def servers = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) => messenger.servers(cloudService)
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  /**
   * Ajax get request for single server
   */
  def show(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) => messenger.show(serverId, cloudService)
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  /**
   * Ajax post for pausing a server
   */
  def powerOff(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) => messenger.powerOff(serverId, cloudService)
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  /**
   * Ajax post for starting a server
   */
  def powerOn(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) => messenger.powerOn(serverId, cloudService)
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  /**
   * Ajax delete for deleting a server
   */
  def delete(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match{
      case Some(cloudService) => messenger.delete(serverId, cloudService)
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  def cloudInfo(apiKey: String): (String, String) = {
    "Cloud-Info" -> apiKey
  }

  def createServer = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(server) => messenger.createServer(server)
      case None => Future.successful(NotFound(Json.obj("error" -> "Api Key not found")))
    }
  }

  /**
   * Ajax get for retrieving the api key
   * @return
   */
  def getApiKey = withAuth { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(server) => Ok(Json.obj("success" -> server.apiKey))
      case None => BadRequest(Json.obj("error" -> "No API-key found"))
    }
  }

  /**
   * Ajax POST for authenticating and saving the api key
   */
  def authenticateApiKey = withAuthAsync { implicit user => implicit request =>
    request.body.asJson.map{ json =>
      WS.url(s"http://$serverManagementUrl/authenticate")
        .withHeaders(cloudInfo((json \ "apiKey").as[String]))
        .get()
        .map(r =>
          r.status match{
            case OK =>
              ServerSettings.findByUserId(user.id) map {serverSettings =>
                serverSettings.copy(apiKey = (json \ "apiKey").as[String]).save()
              } getOrElse ServerSettings.create(user.id, (json \ "apiKey").as[String])
              Ok(r.json)
            case BAD_REQUEST =>
              BadRequest(Json.obj("error" -> "API-Key did not work"))
          }
        )
        .recover {
          case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
          case _ => InternalServerError(unexpectedError)
        }
    } getOrElse Future.successful(BadRequest(Json.obj("error" -> "API-Key needs to be provided")))
  }

  def getServerOptions = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) => messenger.getServerOptions(cloudService)
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  def getSSHKeys = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) => messenger.getSSHKeys(cloudService)
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  def reboot(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) map { cloudService =>
      messenger.reboot(serverId, cloudService)
    } getOrElse Future.successful(Redirect(routes.Application.app("profile")))
  }
}
