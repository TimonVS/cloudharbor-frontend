package api

import controllers.CloudServices._
import models.DigitalOcean.{Droplet, SSHKey}
import models.{ErrorFlash, SuccessFlash}
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc.Flash

import scala.concurrent.Future
import scala.util.{Either, Left, Right}

/**
 * Created by Rudie on 2-5-2015.
 */
trait DigitalOceanAPI {

  val baseUri = "https://api.digitalocean.com/v2/"

  def addDroplet(apiKey: String, droplet: Droplet) =
    WS.url(baseUri + "droplets")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(droplet toJson)
      .map(result => result.status match {
      case ACCEPTED => "success" -> "Droplet created"
      case _ => (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)
    })

  def addSSHKey(apiKey: String, sshKey: SSHKey): Future[Either[BigDecimal, String]] =
    WS.url(baseUri + "account/keys")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(sshKey toJson)
      .map(result => result.status match {
      case CREATED => Left((result.json \ "ssh_key" \ "id").as[BigDecimal])
      case _ => Right((result.json \ "message").as[String])
    })

  def powerOnDroplet(id: String, apiKey: String): Future[Flash] =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_on"))
      .map(result => result.status match {
      case CREATED => SuccessFlash("Server powered on")
      case _ => ErrorFlash((result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString))
    })

  def powerOffDroplet(id: String, apiKey: String): Future[Flash] =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_off"))
      .map(result => result.status match {
      case CREATED => SuccessFlash("Server powered off")
      case _ => ErrorFlash((result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString))
    })

  def deleteDroplet(id: String, apiKey: String): Future[Flash] =
    WS.url(baseUri + s"droplets/$id")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .delete()
      .map(result => result.status match {
      case NO_CONTENT => SuccessFlash("Server deleted")
      case _ => ErrorFlash((result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString))
    })
}
