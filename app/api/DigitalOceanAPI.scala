package api

import models.DigitalOcean.{Droplet, SSHKey}
import models._
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS

import scala.concurrent.Future
import scala.util.{Either, Left, Right}

/**
 * Created by Rudie on 2-5-2015.
 */
trait DigitalOceanAPI {
  type Result[A, B] = Future[Either[Success[A], Error[B]]]

  val baseUri = "https://api.digitalocean.com/v2/"

  def addDroplet(apiKey: String, droplet: Droplet) =
    WS.url(baseUri + "droplets")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(droplet toJson)
      .map(result => result match {
      case play.api.mvc.Results.Accepted => "success" -> "Droplet created"
      case _ => (result.json \ "message")
        .asOpt[String].getOrElse(result.json.toString)
    })

  def addSSHKey(apiKey: String, sshKey: SSHKey): Future[Either[BigDecimal, String]] =
    WS.url(baseUri + "account/keys")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(sshKey toJson)
      .map {
      case result@play.api.mvc.Results.Created => Left((result.json \ "ssh_key" \ "id").as[BigDecimal])
      case error => Right((error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString))
    }

  def powerOnDroplet(id: String, apiKey: String): Result[(String, String), (String, String)] =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_on"))
      .map {
      case play.api.mvc.Results.Created => Left(Success("success" -> "Server powered on"))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  def powerOffDroplet(id: String, apiKey: String): Result[(String, String), (String, String)] =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_off"))
      .map {
      case play.api.mvc.Results.Created => Left(Success("success" -> "Server powered off"))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  def deleteDroplet(id: String, apiKey: String): Result[(String, String), (String, String)] =
    WS.url(baseUri + s"droplets/$id")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .delete()
      .map {
      case play.api.mvc.Results.NoContent => Left(Success("success" -> "Server deleted"))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }
}
