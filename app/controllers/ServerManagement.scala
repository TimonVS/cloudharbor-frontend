package controllers

import java.net.ConnectException
import java.util.concurrent.TimeUnit

import actors.NotificationActor
import api.{CloudAPI, DigitalOceanAPI}
import models.DigitalOcean.{CreateDroplet, CreateSSHKey}
import models._
import play.api.Play.current
import play.api.data.Forms._
import play.api.data._
import play.api.libs.concurrent.Akka
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.{Result, Results, Controller, EssentialAction}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Controller object for every Server related action
 */
object ServerManagement extends Controller with Secured {

  val serverManagementUrl = current.configuration.getString("serverManagement.url").get + ":" +
    current.configuration.getInt("serverManagement.port").get

  val unavailableJsonMessage = Json.obj("error" -> "Sorry, but the Server Management Service is currently not available")
  val unexpectedError = Json.obj("error" -> "Sorry, but an unexpected error occurred")

  def cloudInfo(apiKey: String): (String, String) = {
    "Cloud-Info" -> (apiKey)
  }

  def sendEmptyPost(url: String, apiKey: String): Future[Result] =
    WS.url(url)
      .withHeaders(cloudInfo(apiKey))
      .post(Results.EmptyContent()).map(r => r.status match{
      case NO_CONTENT => Ok(r.json)
      case NOT_MODIFIED => BadRequest(r.json)
      case NOT_FOUND => NotFound(r.json)
      case INTERNAL_SERVER_ERROR => BadRequest(r.json)
    }) recover{
      case _: ConnectException => InternalServerError
    }

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def overview = withAuth { implicit user => implicit request =>
    Ok(views.html.serverManagement.overview())
  }

  def servers = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match {
        case Some(cloudService) =>
          WS.url(s"http://$serverManagementUrl/servers")
            .withHeaders(cloudInfo(cloudService.apiKey))
            .get()
            .map(r => Ok(r.json))
            .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage)
            case _ => InternalServerError(unexpectedError)
          }

        case None =>
          Future.successful(Redirect(routes.ServerManagement.addCloudServiceAccount()))
      }
  }

  def show(serverId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match {
      case Some(cloudService) =>
        WS.url(s"http://$serverManagementUrl/servers/$serverId")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .get()
          .map(r => Ok(r.json))
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage)
            case _ => InternalServerError(unexpectedError)
        }
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addCloudServiceAccount()))
    }
  }

  def powerOff(serverId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match{
      case Some(cloudService) =>
        sendEmptyPost(s"$serverManagementUrl/servers/$serverId/stop", cloudService.apiKey)
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addCloudServiceAccount()))
    }
  }

  def powerOn(serverId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match{
      case Some(cloudService) =>
        sendEmptyPost(s"$serverManagementUrl/servers/$serverId/start", cloudService.apiKey)
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addCloudServiceAccount()))
    }
  }

  def delete(serverId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match{
      case Some(cloudService) =>
        WS.url(s"$serverManagementUrl/servers/$serverId/delete")
          .withHeaders(cloudInfo(cloudService.apiKey))
          .delete()
          .map(r => Ok(r.json))
          .recover {
            case _: ConnectException => BadRequest(unavailableJsonMessage)
            case _ => InternalServerError(unexpectedError)
          }
      case None =>
        Future.successful(Redirect(routes.ServerManagement.addCloudServiceAccount()))
    }
  }

  /**
   * TODO
   * :::::
   */

  lazy val notificationActor = Akka.system.actorOf(NotificationActor.props, name = "notificationActor")

  val regions = Seq(("ams2", "Amsterdam 2"), ("ams3", "Amsterdam 3"))
  val sizes = Seq(
    ("512mb", "512mb - €5 p/month"),
    ("1gb", "1gb - €10 p/month"),
    ("2gb", "2gb - €20 p/month"),
    ("4gb", "4gb - €40 p/month")
  )

  val addServerInfoForm: Form[ApiData] = Form(
    mapping(
      "apiKey" -> text
    )(ApiData.apply)(ApiData.unapply)
  )

  val addServerForm: Form[ServerData] = Form(
    mapping(
      "name" -> text(minLength = 1),
      "region" -> text,
      "size" -> text,
      "backUps" -> boolean,
      "ipv6" -> boolean,
      "ssh" -> boolean,
      "name" -> optional(text),
      "publicKey" -> optional(text)
    )(ServerData.apply)(ServerData.unapply) verifying (
      "If you want to add a ssh key please fill in the name and the public key of the ssh key",
      data => sshCheck(data.ssh, data.sshName, data.sshPublicKey)
      )
  )

  def sshCheck(ssh: Boolean, sshName: Option[String], sshPublicKey: Option[String]): Boolean =
    if(ssh) sshName.isDefined && sshPublicKey.isDefined
    else true

  // Ok view without form
  def addCloudService() = withAuth { implicit user => implicit request =>
    Ok(views.html.serverManagement.addCloudService(addServerForm, regions, sizes))
  }

  // Ok view without form
  def addCloudServiceAccount() = withAuth{ implicit user => implicit request =>
    val cloudService = Server.findByUserId(user.id)
    val addForm: Form[ApiData] = cloudService
      .map(cs => addServerInfoForm.fill(ApiData(cs.apiKey)))
      .getOrElse(addServerInfoForm)

    Ok(views.html.serverManagement.addCloudServiceInfo(addForm))
  }

  // call to ServerManagement where you create a server
  // route in ServerManagement: /servers/add
  def createServer = withAuthAsync { implicit user => implicit request =>
    Future.successful(Ok)
  }

  // call to ServerManagement where you check if the user api key is valid
  // route in ServerManagement: /authenticate
  def authenticateCloudService = withAuthAsync { implicit user => implicit request =>
    Future.successful(Ok)
  }

}
