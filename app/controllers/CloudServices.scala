package controllers

import java.util.concurrent.TimeUnit
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc.{Flash, Controller}
import play.api.Play.current
import CloudServer.personReads
import scala.concurrent.Await
import scala.concurrent.duration.Duration
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
  ) // implement a check to make a request to check if I can get data from digital ocean the make sure the api key is right

  val addServiceForm: Form[CloudServerData] = Form(
    mapping(
      "name" -> text(minLength = 1),
      "region" -> text,
      "size" -> text,
      "backUps" -> boolean,
      "ipv6" -> boolean
    )(CloudServerData.apply)(CloudServerData.unapply)
  )

  def overviewDefault = withAuth{ user => implicit request =>
    Redirect(routes.CloudServices.overview(user.id))
  }

  def overview(userId: Int) = withAuth { user =>
    implicit request =>
      val cloudServiceOpt = CloudService.findByUserId(user.id)
      cloudServiceOpt match{
        case Some(cloudService) => {
          val cloudServersJson = WS.url( "https://api.digitalocean.com/v2/droplets")
            .withHeaders("Authorization" -> ("Bearer " + cloudService.apiKey))
            .get()
          val test: WSResponse = Await.result(cloudServersJson, Duration.create(1.0, TimeUnit.SECONDS))
          Ok(views.html.cloudservices.overview((test.json \ "droplets").as[List[CloudServer]]))
        }
        case None => Redirect(routes.CloudServices.addCloudServiceAccount()).flashing("error" -> "Add your api-key first")
      }
  }

  def show(cloudServiceId: String) = withAuth { user =>
    implicit request =>
      val id = BigDecimal(cloudServiceId)
      val cloudInfoOpt = CloudService.findByUserId(user.id)

      cloudInfoOpt.map{cloudInfo =>
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

  def addCloudServiceAccount = withAuth{ user => implicit request =>
    Ok(views.html.cloudservices.addCloudServiceInfo(addCloudServiceInfoForm))
  }

  def authenticateCloudService = withAuth { user => implicit request =>
    addServiceForm.bindFromRequest().fold(
      formWithErrors => BadRequest(views.html.cloudservices.addCloudService(formWithErrors, regions, sizes)),
      data => {
        CloudService.findByUserId(user.id).map{ cloudService =>
          val jsonData = Json.obj(
            "name" -> data.name,
            "image" -> "docker",
            "region" -> data.region,
            "size" -> data.size,
            "backups" -> data.backUps,
            "ipv6" -> data.ipv6
          )
          val post = WS.url("https://api.digitalocean.com/v2/droplets")
            .withHeaders("Authorization" -> ("Bearer " + cloudService.apiKey))
            .post(jsonData)

          val result = Await.result(post, Duration.create(3.0, TimeUnit.SECONDS))

          if(result.status != 202) Redirect(routes.CloudServices.addCloudService())
              .flashing("error" -> (result.json \ "message").as[String])
          else Redirect(routes.CloudServices.overview(user.id))

        }.getOrElse(Redirect(routes.CloudServices.addCloudService()).flashing("error" -> "Add a cloud service first"))
      }
    )
  }

  def powerOff(cloudServiceId: String) = withAuth{ user => implicit request =>
    val cloudServiceInfo = CloudService.findByUserId(user.id).get
    val getResult = WS.url(s"https://api.digitalocean.com/v2/droplets/$cloudServiceId/actions")
      .withHeaders("Authorization" -> ("Bearer " + cloudServiceInfo.apiKey))
      .post(
        Json.obj("type" -> "power_off")
      )
    val flash = {
      val result = Await.result(getResult, Duration.create(3.0, TimeUnit.SECONDS))
      if(result.status == 201){
        ("succes" -> "Server powered off")
      }else if(result.status == 422){
        (result.json \ "message").asOpt[String].map{ message =>
          ("error" -> message)
        }.getOrElse{
          ("error" -> result.json.toString)
        }
      }
      else{
        ("error" -> (result.json \ "message").as[String])
      }
    }
    Redirect(routes.CloudServices.show(cloudServiceId)).flashing(flash)
  }

  def powerOn(cloudServiceId: String) = withAuth{ user => implicit request =>
    val cloudServiceInfo = CloudService.findByUserId(user.id).get
    val getResult = WS.url(s"https://api.digitalocean.com/v2/droplets/$cloudServiceId/actions")
      .withHeaders("Authorization" -> ("Bearer " + cloudServiceInfo.apiKey))
      .post(
        Json.obj("type" -> "power_on")
      )
    val flash = {
      val result = Await.result(getResult, Duration.create(3.0, TimeUnit.SECONDS))
      if(result.status == 201){
        ("succes" -> "Server started")
      }else if(result.status == 422){
        (result.json \ "message").asOpt[String].map{ message =>
          ("error" -> message)
        }.getOrElse{
          ("error" -> result.json.toString)
        }
      }else{
        ("error" -> (result.json \ "message").as[String])
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
      if(result.status == 204){
        ("succes" -> "Server deleted")
      }else{
        ("error" -> (result.json \ "message").as[String])
      }
    }
    if(flash._1 == "succes") Redirect(routes.CloudServices.overview(user.id)).flashing(flash)
    else Redirect(routes.CloudServices.show(cloudServiceId)).flashing(flash)
  }

  def authenticateCloudServiceInfo = withAuth{ user => implicit request =>
    addCloudServiceInfoForm.bindFromRequest().fold(
      forumWithErrors => BadRequest(views.html.cloudservices.addCloudServiceInfo(forumWithErrors)),
      data => {
        val cloudServiceInfo = CloudService.create(user.id, data.apiKey)
        Redirect(routes.CloudServices.overview(user.id))
      }
    )
  }

}
