package controllers

import java.util.concurrent.TimeUnit
import actors.NotificationActor
import akka.actor.Props
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc.Controller
import play.api.Play.current
import CloudServer.personReads
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.concurrent.Akka
import play.api.libs.json._

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Controller object for every CloudService related action
 */
object CloudServices extends Controller with Secured{

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

  def overview(userId: Int) = withAuth { user =>
    implicit request =>
      val cloudServiceOpt = CloudService.findByUserId(user.id)
      cloudServiceOpt match{
        case Some(cloudService) =>
          val cloudServersJson = WS.url( "https://api.digitalocean.com/v2/droplets")
            .withHeaders("Authorization" -> ("Bearer " + cloudService.apiKey))
            .get()
          val test: WSResponse = Await.result(cloudServersJson, Duration.create(3.0, TimeUnit.SECONDS))
          Ok(views.html.cloudservices.overview((test.json \ "droplets").as[List[CloudServer]]))
        case None => Redirect(routes.CloudServices.addCloudServiceAccount()).flashing("error" -> "Add your api-key first")
      }
  }

  def show(cloudServiceId: String) = withAuth { user =>
    implicit request =>
      val id = BigDecimal(cloudServiceId)

      user.cloudService.map{cloudInfo =>
        val server = WS.url(s"https://api.digitalocean.com/v2/droplets/$id")
          .withHeaders("Authorization" -> ("Bearer " + cloudInfo.apiKey))
          .get()
        val reply = Await.result(server, Duration.create(3.0, TimeUnit.SECONDS))
        val cloudServer = (reply.json \ "droplet").as[CloudServer]

        Ok(views.html.cloudservices.show(cloudServer))

      }.getOrElse(
          Redirect(routes.CloudServices.addCloudService())
            .flashing("error" -> "Please add a digital ocean api-key first")
        )
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

  def authenticateCloudService = withAuth { user => implicit request =>
    addServiceForm.bindFromRequest().fold(
      formWithErrors => BadRequest(views.html.cloudservices.addCloudService(formWithErrors, regions, sizes)),
      data => {
        CloudService.findByUserId(user.id).map{ cloudService =>
          // val sshId is either a String with an error message or an optional BigDecimal
          val sshId: Either[String, Option[BigDecimal]] = if(data.ssh){
            // create json with data from form, get is possible since data.ssh is true
            val sshJson = Json.obj(
              "name" -> data.sshName.get,
              "public_key" -> data.sshPublicKey.get
            )

            // post the ssh key to digital ocean
            val sshPost = WS.url("https://api.digitalocean.com/v2/account/keys")
              .withHeaders("Authorization" -> ("Bearer " + cloudService.apiKey))
              .post(sshJson)

            // wait for the result with a max time of 3 seconds
            val resultSsh = Await.result(sshPost, Duration.create(3.0, TimeUnit.SECONDS))

            // match on the status code
            resultSsh.status match {
              case CREATED => Right(Some((resultSsh.json \ "ssh_key" \ "id").as[BigDecimal])) // send back id
              case _ => Left((resultSsh.json \ "message").as[String]) // send back the error message from the json
            }
          }else{
            Right(None) // if ssh is not enabled the id is Non
          }

          // folding either the error or the actual id value and create a server that has that ssh key
          sshId.fold(
            error => Redirect(routes.CloudServices.addCloudService()).flashing("error" -> error),
            id => {
              val jsonData = Json.obj(
                "name" -> data.name,
                "image" -> "docker",
                "region" -> data.region,
                "size" -> data.size,
                "backups" -> data.backUps,
                "ipv6" -> data.ipv6,
                "ssh_keys" -> Json.arr(id)
              )

              val post = WS.url("https://api.digitalocean.com/v2/droplets")
                .withHeaders("Authorization" -> ("Bearer " + cloudService.apiKey))
                .post(jsonData)

              val result = Await.result(post, Duration.create(3.0, TimeUnit.SECONDS))

              if(result.status != ACCEPTED) Redirect(routes.CloudServices.addCloudService())
                .flashing("error" -> (result.json \ "message").as[String])
              else {
                val serverId = (result.json \ "droplet" \ "id").as[BigDecimal]

                val notificationActor = Akka.system.actorOf(Props[NotificationActor], name = "notificationActor")

                notificationActor ! NotificationActor.CreateServerNotification(
                  user.id,
                  NotificationMessages.serverCreated(serverId),
                  NotificationType.ServerCreate
                )

                Redirect(routes.CloudServices.overview(user.id))
              }
            }
          )
        }.getOrElse(Redirect(routes.CloudServices.addCloudService()).flashing("error" -> "Add a cloud service first"))
      }
    )
  }

  def powerOff(cloudServiceId: String) = withAuth{ user => implicit request =>
    val cloudServiceInfo = CloudService.findByUserId(user.id).get
    val getResult = WS.url(s"https://api.digitalocean.com/v2/droplets/$cloudServiceId/actions")
      .withHeaders("Authorization" -> ("Bearer " + cloudServiceInfo.apiKey))
      .post(Json.obj("type" -> "power_off"))
    val flash = {
      val result = Await.result(getResult, Duration.create(3.0, TimeUnit.SECONDS))

      result.status match {
        case CREATED => "succes" -> "Server powered off"
        case _ => (result.json \ "message").asOpt[String]
          .map(message => "error" -> message)
          .getOrElse("error" -> result.json.toString)
      }
    }
    Redirect(routes.CloudServices.show(cloudServiceId)).flashing(flash)
  }

  def powerOn(cloudServiceId: String) = withAuth{ user => implicit request =>
    val cloudServiceInfo = CloudService.findByUserId(user.id).get
    val getResult = WS.url(s"https://api.digitalocean.com/v2/droplets/$cloudServiceId/actions")
      .withHeaders("Authorization" -> ("Bearer " + cloudServiceInfo.apiKey))
      .post(Json.obj("type" -> "power_on"))
    val flash = {
      val result = Await.result(getResult, Duration.create(3.0, TimeUnit.SECONDS))

      result.status match {
        case CREATED => "succes" -> "Server started"
        case _ => (result.json \ "message").asOpt[String]
          .map(message => "error" -> message)
          .getOrElse("error" -> result.json.toString)
      }
    }
    Redirect(routes.CloudServices.show(cloudServiceId)).flashing(flash)
  }

  def delete(cloudServiceId: String) = withAuth{ user => implicit request =>
    val cloudServiceInfo = CloudService.findByUserId(user.id).get
    val getResult = WS.url(s"https://api.digitalocean.com/v2/droplets/$cloudServiceId")
      .withHeaders("Authorization" -> ("Bearer " + cloudServiceInfo.apiKey))
      .delete()
    val flash = {
      val result = Await.result(getResult, Duration.create(3.0, TimeUnit.SECONDS))
      if(result.status == NO_CONTENT) "succes" -> "Server deleted"
      else "error" -> (result.json \ "message").as[String]
    }
    if(flash._1 == "succes") {
      val notificationActor = Akka.system.actorOf(Props[NotificationActor], name = "notificationActor")

      notificationActor ! NotificationActor.CreateServerNotification(
        user.id,
        NotificationMessages.serverDeleted(BigDecimal(cloudServiceId)),
        NotificationType.ServerDelete
      )
      Redirect(routes.CloudServices.overview(user.id)).flashing(flash)
    }
    else Redirect(routes.CloudServices.show(cloudServiceId)).flashing(flash)
  }

  def authenticateCloudServiceInfo = withAuth { user => implicit request =>
    addCloudServiceInfoForm.bindFromRequest().fold(
      forumWithErrors => BadRequest(views.html.cloudservices.addCloudServiceInfo(forumWithErrors)),
      data => {
        val request = WS.url("https://api.digitalocean.com/v2/account")
          .withHeaders("Authorization" -> ("Bearer " + data.apiKey))
          .get()

        val result = Await.result(request, Duration.create(3.0, TimeUnit.SECONDS))

        val works = if(result.status == OK) true else false

        if (works) {
          val cloudServiceOpt = CloudService.findByUserId(user.id)
          val cloudServiceInfo = cloudServiceOpt.map { cs =>
            cs.copy(apiKey = data.apiKey).save()
          }.getOrElse {
            CloudService.create(user.id, data.apiKey)
          }
          Redirect(routes.CloudServices.overview(user.id))
        }
        else Redirect(routes.CloudServices.addCloudServiceAccount()).flashing("error" -> "Api-key did not work!")
      }
    )
  }

}
