package controllers

import java.util.concurrent.TimeUnit

import actors.NotificationActor
import api.{CloudAPI, DigitalOceanAPI}
import models.DigitalOcean.{CreateDroplet, CreateSSHKey}
import models._
import play.api.Play.current
import play.api.data.Forms._
import play.api.data._
import play.api.libs.concurrent.Akka
import play.api.mvc.{Controller, EssentialAction}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Controller object for every Server related action
 */
object ServerManagement extends Controller with Secured {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  lazy val cloudAPI: CloudAPI = DigitalOceanAPI
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


  def overviewDefault = withAuth{ implicit user => implicit request =>
    Redirect(routes.ServerManagement.overview(user.id))
  }

  def overview(userId: Int) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match {
        case Some(cloudService) =>
          cloudAPI.getCloudServers(cloudService.apiKey).map(result => result.fold(
            success => Ok(views.html.serverManagement.overview(success.data)),
            error => Redirect(routes.ServerManagement.addCloudServiceAccount()).flashing(error.data)
          ))
        case None => Future(Redirect(routes.ServerManagement.addCloudServiceAccount())
          .flashing("error" -> "Add your api-key first"))
      }
  }

  def show(cloudServiceId: String) = withAuthAsync { implicit user => implicit request =>
    Server.findByUserId(user.id) match {
      case Some(cloudService) =>
        cloudAPI.getCloudServer(BigDecimal(cloudServiceId), cloudService.apiKey).map(result => result.fold(
          success => Ok(views.html.serverManagement.show(success.data)),
          error => Redirect(routes.ServerManagement.addCloudServiceAccount()).flashing(error.data)
        ))
      case None =>
        Future(Redirect(routes.ServerManagement.addCloudService())
          .flashing("error" -> "Please add a digital ocean api-key first"))
    }
  }

  def addCloudService() = withAuth { implicit user => implicit request =>
    Ok(views.html.serverManagement.addCloudService(addServerForm, regions, sizes))
  }

  def addCloudServiceAccount() = withAuth{ implicit user => implicit request =>
    val cloudService = Server.findByUserId(user.id)
    val addForm: Form[ApiData] = cloudService
      .map(cs => addServerInfoForm.fill(ApiData(cs.apiKey)))
      .getOrElse(addServerInfoForm)

    Ok(views.html.serverManagement.addCloudServiceInfo(addForm))
  }

  def authenticateCloudService = withAuthAsync { implicit user => implicit request =>
    addServerForm.bindFromRequest().fold(
      formWithErrors => Future(BadRequest(views.html.serverManagement.addCloudService(formWithErrors, regions, sizes))),
      data => {
        Server.findByUserId(user.id) match {
          case Some(cloudService) =>
            val apiKey = cloudService.apiKey

            def create(sshKeys: Option[List[BigDecimal]]) = {
              //TODO: add specified token and existing ssh keys
              val userData = Source.fromFile(getClass.getResource("/cloud-config.yaml").toURI).mkString
              val droplet = CreateDroplet(data.name, "coreos-stable", data.region, data.size, data.backUps, data.ipv6, userData, sshKeys)
              cloudAPI.createServer(apiKey, droplet).map(result => result.fold(
                success => {
                  notificationActor ! NotificationActor.CreateServerNotification(
                    user.id,
                    NotificationMessages.serverCreated(success.data),
                    NotificationType.ServerCreate
                  )

                  Redirect(routes.ServerManagement.overview(user.id))
                },
                error => Redirect(routes.ServerManagement.addCloudService()).flashing(error.data)
              ))
            }

            if (data.ssh) {
              val sshKey = CreateSSHKey(data.sshName.get, data.sshPublicKey.get)
              val result = Await.result(cloudAPI.addSSHKey(apiKey, sshKey), Duration.create(3, TimeUnit.SECONDS))
              result.fold(
                success => create(Some(List(success.data))),
                error => Future(Redirect(routes.ServerManagement.addCloudService()).flashing(error.data))
              )
            } else {
              create(None)
            }
          case None => Future(Redirect(routes.ServerManagement.addCloudService()).flashing("error" -> "Add a cloud service first"))
        }
      }
    )
  }

  def powerOff(cloudServiceId: String) = withAuthAsync { implicit user => implicit request =>
    val apiKey = Server.findByUserId(user.id).get.apiKey

    cloudAPI.powerOff(cloudServiceId, apiKey).map(result => result.fold(
      success => Redirect(routes.ServerManagement.show(cloudServiceId)).flashing(success.data),
      error => Redirect(routes.ServerManagement.show(cloudServiceId)).flashing(error.data)
    ))
  }

  def powerOn(cloudServiceId: String): EssentialAction = withAuthAsync { implicit user => implicit request =>
    val apiKey = Server.findByUserId(user.id).get.apiKey

    cloudAPI.powerOn(cloudServiceId, apiKey).map(result => result.fold(
      success => Redirect(routes.ServerManagement.show(cloudServiceId)).flashing(success.data),
      error => Redirect(routes.ServerManagement.show(cloudServiceId)).flashing(error.data)
    ))
  }

  def delete(cloudServiceId: String) = withAuthAsync { implicit user => implicit request =>
    val apiKey = Server.findByUserId(user.id).get.apiKey

    DigitalOceanAPI.delete(cloudServiceId, apiKey).map(result => result.fold(
      success => {
        notificationActor ! NotificationActor.CreateServerNotification(
          user.id,
          NotificationMessages.serverDeleted(BigDecimal(cloudServiceId)),
          NotificationType.ServerDelete
        )

        Redirect(routes.ServerManagement.overview(user.id)).flashing(success.data)
      },
      error => Redirect(routes.ServerManagement.show(cloudServiceId)).flashing(error.data)
    ))
  }

  def authenticateCloudServiceInfo = withAuthAsync { implicit user => implicit request =>
    addServerInfoForm.bindFromRequest().fold(
      forumWithErrors => Future(BadRequest(views.html.serverManagement.addCloudServiceInfo(forumWithErrors))),
      data => {
        cloudAPI.authenticate(data.apiKey).map(result => result.fold(
          success => {
            Server.findByUserId(user.id) match {
              case Some(cloudService) => cloudService.copy(apiKey = data.apiKey).save()
              case None => Server.create(user.id, data.apiKey)
            }
            Redirect(routes.ServerManagement.overview(user.id))
          },
          error => Redirect(routes.ServerManagement.addCloudServiceAccount()).flashing("error" -> "Api-key did not work!")))
      }
    )
  }

}
