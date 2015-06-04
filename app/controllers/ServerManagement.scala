package controllers

import java.net.ConnectException
import actors.NotificationActor
import models._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.Controller
import utils.WsUtils
import scala.concurrent.{Future}

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Controller object for every Server related action
 */
object ServerManagement extends Controller with Secured with WsUtils {

  val serverManagementUrl = current.configuration.getString("serverManagement.url").get + ":" +
    current.configuration.getInt("serverManagement.port").get

  val SERVER_MANAGEMENT = "Server Management"

  lazy val notificationActor = Akka.system.actorOf(NotificationActor.props, name = "notificationActor")

  def cloudInfo(apiKey: String): (String, String) = {
    "Cloud-Info" -> (apiKey)
  }

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  /**
   * Static html page for server overview
   */
  def overview = withAuth { implicit user => implicit request =>
    Ok(views.html.serverManagement.overview())
  }

  /**
   * Ajax get request for every server a user has at digital ocean
   */
  def servers = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match {
        case Some(cloudService) =>
          WS.url(s"http://$serverManagementUrl/servers")
            .withHeaders(cloudInfo(cloudService.apiKey))
            .get()
            .map(forwardResponse(_))
            .recover {
              case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
              case _ => InternalServerError(unexpectedError)
            }

        case None =>
          Future.successful(Redirect(routes.ServerManagement.addApiKey()))
      }
  }

  /**
   * Ajax get request for single server
   */
  def show(serverId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match {
      case Some(cloudService) =>
        WS.url(s"http://$serverManagementUrl/servers/$serverId")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .get()
          .map(forwardResponse(_))
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
            case _ => InternalServerError(unexpectedError)
        }
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addApiKey()))
    }
  }

  /**
   * Ajax post for pausing a server
   */
  def powerOff(serverId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match{
      case Some(cloudService) =>
        sendEmptyPost(s"$serverManagementUrl/servers/$serverId/stop", cloudService.apiKey, cloudInfo, SERVER_MANAGEMENT)
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addApiKey()))
    }
  }

  /**
   * Ajax post for starting a server
   */
  def powerOn(serverId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match{
      case Some(cloudService) =>
        sendEmptyPost(s"$serverManagementUrl/servers/$serverId/start", cloudService.apiKey, cloudInfo, SERVER_MANAGEMENT)
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addApiKey()))
    }
  }

  /**
   * Ajax delete for deleting a server
   */
  def delete(serverId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match{
      case Some(cloudService) =>
        WS.url(s"$serverManagementUrl/servers/$serverId/delete")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .delete()
          .map(forwardResponse(_))
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
            case _ => InternalServerError(unexpectedError)
          }
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addApiKey()))
    }
  }

  /**
   * Static html page for adding a server
   */
  def addServer() = withAuth { implicit user => implicit request =>
    Ok(views.html.serverManagement.addCloudService())
  }

  def createServer = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match {
      case Some(server) =>
        WS.url(s"http://$serverManagementUrl/servers/add")
          .withHeaders(cloudInfo(server.apiKey))
          .post(request.body.asJson.get)
          .map(forwardResponse(_))
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
            case _ => InternalServerError(unexpectedError)
          }
      case None => Future.successful(NotFound(Json.obj("error" -> "Api Key not found")))
    }
  }

  /**
   * Static html page for adding a api key
   */
  def addApiKey() = withAuth{ implicit user => implicit request =>
    Ok(views.html.serverManagement.addCloudServiceInfo())
  }

  /**
   * Ajax get for retrieving the api key
   * @return
   */
  def getApiKey() = withAuth { implicit user => implicit request =>
    Server.findByUserId(user.id) match {
      case Some(server) => Ok(Json.obj("success" -> server.apiKey))
      case None => BadRequest(Json.obj("error" -> "No API-key found"))
    }
  }

  /**
   * Ajax POST for authenticating and saving the api key
   */
  def authenticateApiKey = withAuthAsync { implicit user => implicit request =>
    request.body.asJson.map{ json =>
      WS.url(s"$serverManagementUrl/authenticate")
        .withHeaders(cloudInfo((json \ "apiKey").as[String]))
        .get()
        .map(r =>
          r.status match{
            case OK =>
              Server.create(user.id, (json \ "apiKey").as[String])
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
    Server.findByUserId(user.id) match {
      case Some(cloudService) =>
        WS.url(s"http://$serverManagementUrl/server-options")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .get()
          .map(forwardResponse(_))
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
            case _ => InternalServerError(unexpectedError)
          }
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addApiKey()))
    }
  }

}
