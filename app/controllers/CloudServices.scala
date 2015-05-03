package controllers

import java.util.concurrent.TimeUnit

import api.DigitalOceanAPI
import models.DigitalOcean.{Droplet, SSHKey}
import models._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{Controller, EssentialAction}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Controller object for every CloudService related action
 */
object CloudServices extends Controller with Secured with DigitalOceanAPI {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val regions = Seq(("ams2", "Amsterdam 2"), ("ams3", "Amsterdam 3"))
  val sizes = Seq(
    ("512mb", "512mb - €5 p/month"),
    ("1gb", "1gb - €10 p/month"),
    ("2gb", "2gb - €20 p/month"),
    ("4gb", "4gb - €40 p/month")
  )

  val addCloudServiceInfoForm: Form[ApiData] = Form(
    mapping(
      "apiKey" -> text
    )(ApiData.apply)(ApiData.unapply)
  )

  val addServiceForm: Form[CloudServerData] = Form(
    mapping(
      "name" -> text(minLength = 1),
      "region" -> text,
      "size" -> text,
      "backUps" -> boolean,
      "ipv6" -> boolean,
      "ssh" -> boolean,
      "name" -> optional(text),
      "publicKey" -> optional(text)
    )(CloudServerData.apply)(CloudServerData.unapply) verifying (
      "If you want to add a ssh key please fill in the name and the public key of the ssh key",
      data => sshCheck(data.ssh, data.sshName, data.sshPublicKey)
      )
  )

  def sshCheck(ssh: Boolean, sshName: Option[String], sshPublicKey: Option[String]): Boolean =
    if(ssh) sshName.isDefined && sshPublicKey.isDefined
    else true


  def overviewDefault = withAuth{ user => implicit request =>
    Redirect(routes.CloudServices.overview(user.id))
  }

  def overview(userId: Int) = withAuthAsync { user => implicit request =>
    CloudService.findByUserId(user.id) match {
        case Some(cloudService) =>
          getDroplets(cloudService.apiKey).map(result => result.fold(
            success => Ok(views.html.cloudservices.overview(success.data)),
            error => Redirect(routes.CloudServices.addCloudServiceAccount()).flashing(error.data)
          ))
        case None => Future(Redirect(routes.CloudServices.addCloudServiceAccount())
          .flashing("error" -> "Add your api-key first"))
      }
  }

  def show(cloudServiceId: String) = withAuthAsync { user => implicit request =>
    CloudService.findByUserId(user.id) match {
      case Some(cloudService) =>
        getDroplet(BigDecimal(cloudServiceId), cloudService.apiKey).map(result => result.fold(
          success => Ok(views.html.cloudservices.show(success.data)),
          error => Redirect(routes.CloudServices.addCloudServiceAccount()).flashing(error.data)
        ))
      case None =>
        Future(Redirect(routes.CloudServices.addCloudService())
          .flashing("error" -> "Please add a digital ocean api-key first"))
    }
  }
  
  def addCloudService() = withAuth { user => implicit request =>
    Ok(views.html.cloudservices.addCloudService(addServiceForm, regions, sizes))
  }

  def addCloudServiceAccount() = withAuth{ user => implicit request =>
    val cloudService = CloudService.findByUserId(user.id)
    val addForm: Form[ApiData] = cloudService
      .map(cs => addCloudServiceInfoForm.fill(ApiData(cs.apiKey)))
      .getOrElse(addCloudServiceInfoForm)

    Ok(views.html.cloudservices.addCloudServiceInfo(addForm))
  }

  def authenticateCloudService = withAuthAsync { user => implicit request =>
    addServiceForm.bindFromRequest().fold(
      formWithErrors => Future(BadRequest(views.html.cloudservices.addCloudService(formWithErrors, regions, sizes))),
      data => {
        CloudService.findByUserId(user.id) match {
          case Some(cloudService) =>
            val apiKey = cloudService.apiKey

            def create(sshKeys: Option[List[BigDecimal]]) = {
              val droplet = Droplet(data.name, "docker", data.region, data.size, data.backUps, data.ipv6, sshKeys)
              createDroplet(apiKey, droplet).map(result => result.fold(
                success => Redirect(routes.CloudServices.overview(user.id)).flashing(success.data),
                error => Redirect(routes.CloudServices.addCloudService()).flashing(error.data)
              ))
            }

            if (data.ssh) {
              val sshKey = SSHKey(data.sshName.get, data.sshPublicKey.get)
              val result = Await.result(addSSHKey(apiKey, sshKey), Duration.create(3, TimeUnit.SECONDS))
              result.fold(
                success => create(Some(List(success.data))),
                error => Future(Redirect(routes.CloudServices.addCloudService()).flashing(error.data))
              )
            } else {
              create(None)
            }
          case None => Future(Redirect(routes.CloudServices.addCloudService()).flashing("error" -> "Add a cloud service first"))
        }
      }
    )
  }

  def powerOff(cloudServiceId: String) = withAuthAsync { user => implicit request =>
    val apiKey = CloudService.findByUserId(user.id).get.apiKey

    powerOffDroplet(cloudServiceId, apiKey).map(result => result.fold(
      success => Redirect(routes.CloudServices.show(cloudServiceId)).flashing(success.data),
      error => Redirect(routes.CloudServices.show(cloudServiceId)).flashing(error.data)
    ))
  }

  def powerOn(cloudServiceId: String): EssentialAction = withAuthAsync { user => implicit request =>
    val apiKey = CloudService.findByUserId(user.id).get.apiKey

    powerOnDroplet(cloudServiceId, apiKey).map(result => result.fold(
      success => Redirect(routes.CloudServices.show(cloudServiceId)).flashing(success.data),
      error => Redirect(routes.CloudServices.show(cloudServiceId)).flashing(error.data)
    ))
  }

  def delete(cloudServiceId: String) = withAuthAsync { user => implicit request =>
    val apiKey = CloudService.findByUserId(user.id).get.apiKey

    deleteDroplet(cloudServiceId, apiKey).map(result => result.fold(
      success => Redirect(routes.CloudServices.overview(user.id)).flashing(success.data),
      error => Redirect(routes.CloudServices.show(cloudServiceId)).flashing(error.data)
    ))
  }

  def authenticateCloudServiceInfo = withAuthAsync { user => implicit request =>
    addCloudServiceInfoForm.bindFromRequest().fold(
      forumWithErrors => Future(BadRequest(views.html.cloudservices.addCloudServiceInfo(forumWithErrors))),
      data => {
        authenticate(data.apiKey).map(result => result.fold(
          success => {
            CloudService.findByUserId(user.id) match {
              case Some(cloudService) => cloudService.copy(apiKey = data.apiKey).save()
              case None => CloudService.create(user.id, data.apiKey)
            }
            Redirect(routes.CloudServices.overview(user.id))
          },
          error => Redirect(routes.CloudServices.addCloudServiceAccount()).flashing("error" -> "Api-key did not work!")))
      }
    )
  }

}
