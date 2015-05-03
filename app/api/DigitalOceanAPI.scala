package api

import models.DigitalOcean.{Droplet, SSHKey}
import models._
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.mvc.Http.Status._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Left, Right}

/**
 * Created by Rudie on 2-5-2015.
 */
object DigitalOceanAPI extends CloudAPI {
  val baseUri = "https://api.digitalocean.com/v2/"

  @Override
  def authenticate(apiKey: String) =
    WS.url("https://api.digitalocean.com/v2/account")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .get()
      .map(result => result.status match {
      case OK => Left(Success("success" -> "Authenticated"))
      case error => Right(Error("error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)))
    })

  @Override
  def getCloudServer(id: BigDecimal, apiKey: String) =
    WS.url(baseUri + s"droplets/$id")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .get()
      .map(result => result.status match {
      case OK => Left(Success((result.json \ "droplet").as[CloudServer]))
      case error => Right(Error("error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)))
    })

  @Override
  def getCloudServers(apiKey: String) =
    WS.url(baseUri + "droplets")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .get()
      .map(result => result.status match {
      case OK => Left(Success((result.json \ "droplets").as[List[CloudServer]]))
      case error => Right(Error("error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)))
    })

  @Override
  def createService(apiKey: String, droplet: Droplet) =
    WS.url(baseUri + "droplets")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(droplet toJson)
      .map(result => result.status match {
      case ACCEPTED => Left(Success("success" -> "Droplet created"))
      case error => Right(Error("error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)))
    })

  @Override
  def addSSHKey(apiKey: String, sshKey: SSHKey) =
    WS.url(baseUri + "account/keys")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(sshKey toJson)
      .map(result => result.status match {
      case CREATED => Left(Success((result.json \ "ssh_key" \ "id").as[BigDecimal]))
      case error => Right(Error("error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)))
    })

  @Override
  def powerOn(id: String, apiKey: String) =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_on"))
      .map(result => result.status match {
      case CREATED => Left(Success("success" -> "Server powered on"))
      case error => Right(Error("error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)))
    })

  @Override
  def powerOff(id: String, apiKey: String) =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_off"))
      .map(result => result.status match {
      case CREATED => Left(Success("success" -> "Server powered off"))
      case error => Right(Error("error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)))
    })

  @Override
  def delete(id: String, apiKey: String) =
    WS.url(baseUri + s"droplets/$id")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .delete()
      .map(result => result.status match {
      case NO_CONTENT => Left(Success("success" -> "Server deleted"))
      case error => Right(Error("error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)))
    })
}
