package controllers

import java.net.ConnectException

import models._
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.Controller
import utils.WsUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Controller object for every Server related action
 */
object ServerManagement extends Controller with Secured with WsUtils with ServerManagementNotifications {

  val serverManagementUrl = current.configuration.getString("serverManagement.url").get + ":" + current.configuration.getInt("serverManagement.port").get
  val SERVER_MANAGEMENT = "Server Management"


  def ping = withAuthAsync { implicit user => implicit request =>
    WS.url(s"http://$serverManagementUrl/ping")
      .get()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => InternalServerError(unavailableJsonMessage(SERVER_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }
  }

  /**
   * Ajax get request for every server a user has at digital ocean
   */
  def servers = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) =>
        WS.url(s"http://$serverManagementUrl/servers")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .get()
          .map(forwardResponse)
          .recover {
          case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
          case _ => InternalServerError(unexpectedError)
        }

      case None =>
        Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  /**
   * Ajax get request for single server
   */
  def show(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) =>
        WS.url(s"http://$serverManagementUrl/servers/$serverId")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .get()
          .map(forwardResponse)
          .recover {
          case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
          case _ => InternalServerError(unexpectedError)
        }
      case None =>
        Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  /**
   * Ajax post for pausing a server
   */
  def powerOff(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) =>
        forwardPost(s"http://$serverManagementUrl/servers/$serverId/stop", cloudService.apiKey, cloudInfo, SERVER_MANAGEMENT)
      case None =>
        Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  /**
   * Ajax post for starting a server
   */
  def powerOn(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) =>
        forwardPost(s"http://$serverManagementUrl/servers/$serverId/start", cloudService.apiKey, cloudInfo, SERVER_MANAGEMENT)
      case None =>
        Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  /**
   * Ajax delete for deleting a server
   */
  def delete(serverId: String) = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match{
      case Some(cloudService) =>
        WS.url(s"http://$serverManagementUrl/servers/$serverId/delete")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .delete()
          .map(forwardResponse)
          .recover {
          case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
          case _ => InternalServerError(unexpectedError)
        }
      case None =>
        Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  def createServer = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(server) =>
        WS.url(s"http://$serverManagementUrl/servers/add")
          .withHeaders(cloudInfo(server.apiKey))
          .post(request.body.asJson.get)
          .map { response => response.status match {
          case CREATED => notifyServerCreated(user.id, (response.json \ "success").as[String], server.apiKey)
        }
          forwardResponse(response)
        }
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
            case _ => InternalServerError(unexpectedError)
          }
      case None => Future.successful(NotFound(Json.obj("error" -> "Api Key not found")))
    }
  }

  def cloudInfo(apiKey: String): (String, String) = {
    "Cloud-Info" -> apiKey
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
      case Some(cloudService) =>
        WS.url(s"http://$serverManagementUrl/server-options")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .get()
          .map(forwardResponse)
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
            case _ => InternalServerError(unexpectedError)
          }
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }

  def getSSHKeys = withAuthAsync { implicit user => implicit request =>
    ServerSettings.findByUserId(user.id) match {
      case Some(cloudService) =>
        WS.url(s"http://$serverManagementUrl/ssh")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .get()
          .map(forwardResponse)
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
            case _ => InternalServerError(unexpectedError)
        }
      case None => Future.successful(Redirect(routes.Application.app("profile")))
    }
  }
}
