package controllers

import java.net.ConnectException
import actors.NotificationActor.Error
import models.Notifications.ErrorNotification
import models.{ServerSettings, User}
import play.api.Play._
import play.api.libs.ws.WS
import play.api.mvc.{Result, AnyContent, Request}
import play.api.mvc.Results._
import utils.{ServerManagementNotifications, WsUtils}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.http.Status._

/**
 * Created by ThomasWorkBook on 24/06/15.
 */
object HttpMessenger extends Messenger with WsUtils with ServerManagementNotifications {
  val serverManagementUrl = current.configuration.getString("serverManagement.url").get + ":" + current.configuration.getInt("serverManagement.port").get
  val SERVER_MANAGEMENT = "Server Management"

  override def ping(implicit user: User, request: Request[AnyContent]) =
    WS.url(s"http://$serverManagementUrl/ping")
    .get()
    .map(forwardResponse)
    .recover {
      case _: ConnectException => InternalServerError(unavailableJsonMessage(SERVER_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }

  override def servers(cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]) =
    WS.url(s"http://$serverManagementUrl/servers")
      .withHeaders(cloudInfo(cloudService.apiKey))
      .get()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }

  override def show(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]) =
    WS.url(s"http://$serverManagementUrl/servers/$serverId")
      .withHeaders(cloudInfo(cloudService.apiKey))
      .get()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }

  override def powerOff(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]) =
    forwardPost(s"http://$serverManagementUrl/servers/$serverId/stop", cloudService.apiKey, cloudInfo, SERVER_MANAGEMENT)

  override def powerOn(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]) =
    forwardPost(s"http://$serverManagementUrl/servers/$serverId/start", cloudService.apiKey, cloudInfo, SERVER_MANAGEMENT)

  override def delete(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]) =
    WS.url(s"http://$serverManagementUrl/servers/$serverId/delete")
      .withHeaders(cloudInfo(cloudService.apiKey))
      .delete()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }

  override def createServer(cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result] =
    WS.url(s"http://$serverManagementUrl/servers/add")
      .withHeaders(cloudInfo(cloudService.apiKey))
      .post(request.body.asJson.get)
      .map { response =>
        response.status match {
          case OK => notifyServerCreated(user.id, (response.json \ "success").as[String], cloudService.apiKey)
          case _ => notificationActor ! Error(user.id, ErrorNotification("Server could not be created"))
        }
        forwardResponse(response)
      }.recover {
        case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
        case _ => InternalServerError(unexpectedError)
      }

  override def getServerOptions(cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result] =
    WS.url(s"http://$serverManagementUrl/server-options")
      .withHeaders(cloudInfo(cloudService.apiKey))
      .get()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }

  override def getSSHKeys(cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result] =
    WS.url(s"http://$serverManagementUrl/ssh")
      .withHeaders(cloudInfo(cloudService.apiKey))
      .get()
      .map(forwardResponse)
      .recover {
        case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
        case _ => InternalServerError(unexpectedError)
      }

  override def reboot(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result] =
    WS.url(s"http://$serverManagementUrl/servers/$serverId/reboot")
      .withHeaders(cloudInfo(cloudService.apiKey))
      .post(EmptyContent())
      .map{ response =>
        response.status match{
          case CREATED => notifyServerRebooted(user.id, (response.json \ "id").as[BigDecimal], cloudService.apiKey, serverId)
        }
        forwardResponse(response)
      }.recover {
        case _: ConnectException => BadRequest(unavailableJsonMessage(SERVER_MANAGEMENT))
        case _ => InternalServerError(unexpectedError)
      }

}
