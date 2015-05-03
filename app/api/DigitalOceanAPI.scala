package api

import api.DigitalOceanAPI._
import controllers.CloudServices._
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS

import scala.concurrent.Future
import scala.util.{Either, Left, Right}

/**
 * Created by Rudie on 2-5-2015.
 */
object DigitalOceanAPI {

  case class SSHKey(name: String, publicKey: String) {
    def toJson = Json.obj(
      "name" -> name,
      "public_key" -> publicKey
    )
  }

  case class Droplet(name: String, image: String, region: String, size: String, backups: Boolean, ipv6: Boolean, sshKeys: Option[List[BigDecimal]]) {
    def toJson = Json.obj(
      "name" -> name,
      "image" -> image,
      "region" -> region,
      "size" -> size,
      "backups" -> backups,
      "ipv6" -> ipv6,
      "ssh_keys" -> Json.arr(sshKeys)
    )
  }

}

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

  def powerOnDroplet(id: String, apiKey: String): Future[(String, String)] =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_on"))
      .map(result => result.status match {
      case CREATED => "success" -> "Server powered on"
      case _ => "error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)
    })

  def powerOffDroplet(id: String, apiKey: String): Future[(String, String)] =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_off"))
      .map(result => result.status match {
      case CREATED => "success" -> "Server powered off"
      case _ => "error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)
    })

  def deleteDroplet(id: String, apiKey: String): Future[(String, String)] =
    WS.url(baseUri + s"droplets/$id")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .delete()
      .map(result => result.status match {
      case NO_CONTENT => "success" -> "Server deleted"
      case _ => "error" -> (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)
    })
}
