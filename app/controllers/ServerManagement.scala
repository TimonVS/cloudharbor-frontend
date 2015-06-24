package controllers

import models._
import play.api.Play.current
import play.api.libs.json.{JsValue, JsObject, Json}
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc.{Result, Controller, Results}
import utils.{Secured, ServerManagementNotifications, WsUtils}
import scalaz._
import scalaz.Scalaz._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

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
    val serverManagementResult = for {
      json <- request.body.asJson |> fromOption((BadRequest(jsonError("Body of request needs to be http aplication/json"))))
      apiKey <- extractJson(json, "apiKey") |> fromEither(BadRequest(_))
      response <- getRequest(s"http://$serverManagementUrl/authenticate", cloudInfo(apiKey)) |> fromFuture
      serverSettings <- processResult[ServerSettings](response, saveServerSettings(user.id, apiKey) _) |> fromEither(BadRequest(_))
    } yield Ok("API Key is succesfully authenticated")

    serverManagementResult.run.map { _.merge }
  }

  def extractJson(json: JsValue, field: String): JsObject \/ String =
    (json \ field).asOpt[String] map(\/-(_)) getOrElse -\/(Json.obj("error" -> s"$field needs to be provided in json."))

  def getRequest(url: String, headers: (String, String)*): Future[WSResponse] =
    WS.url(url)
      .withHeaders(headers: _*)
      .get()

  def processResult[T](response: WSResponse, f: WSResponse => JsObject \/ T): JsObject \/ T = f(response)

  def fromOption[T](failure: => Result)(ot: Option[T]): EitherT[Future, Result, T] = EitherT(Future.successful(ot \/> failure))

  def fromEither[A, B](failure: B => Result)(va: B \/ A): EitherT[Future, Result, A] = EitherT(Future.successful(va.leftMap(failure)))

  def fromFuture[T](ft: Future[T]): EitherT[Future, Result, T] = EitherT(ft.map(\/.right(_)))

  private def saveServerSettings(userId: Int, apiKey: String)(response: WSResponse): JsObject \/ ServerSettings =
    response.status match {
      case OK =>
        val serverSettings = ServerSettings.findByUserId(userId) map { serverSettings =>
          serverSettings.copy(apiKey = apiKey)
        } getOrElse ServerSettings.create(userId, apiKey)

        \/-(serverSettings)
      case BAD_REQUEST => -\/(Json.obj("error" -> "API-Key was unsuccessfully authenticated."))
    }

  def jsonError(message: String): JsObject = Json.obj("error" -> message)

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
