package api

import models.DigitalOcean.{Droplet, SSHKey}
import models._
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Either, Left, Right}

/**
 * Created by Rudie on 2-5-2015.
 */
trait DigitalOceanAPI {
  type Result[A, B] = Future[Either[Success[A], Error[B]]]

  val baseUri = "https://api.digitalocean.com/v2/"

  def authenticate(apiKey: String): Result[(String, String), (String, String)] =
    WS.url("https://api.digitalocean.com/v2/account")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .get()
      .map {
      case result@play.api.mvc.Results.Ok => Left(Success("success" -> "Authenticated"))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  /** Retrieves a droplet. */
  def getDroplet(id: BigDecimal, apiKey: String): Result[CloudServer, (String, String)] =
    WS.url(baseUri + s"droplets/$id")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .get()
      .map {
      case result@play.api.mvc.Results.Ok => Left(Success((result.json \ "droplet").as[CloudServer]))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  def getDroplets(apiKey: String): Result[List[CloudServer], (String, String)] =
    WS.url(baseUri + "droplets")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .get()
      .map {
      case result@play.api.mvc.Results.Ok => Left(Success((result.json \ "droplets").as[List[CloudServer]]))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  /** Creates a droplet. */
  def createDroplet(apiKey: String, droplet: Droplet): Result[(String, String), (String, String)] =
    WS.url(baseUri + "droplets")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(droplet toJson)
      .map {
      case play.api.mvc.Results.Accepted => Left(Success("success" -> "Droplet created"))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  /** Adds a SSH key to the Digital Ocean account. */
  def addSSHKey(apiKey: String, sshKey: SSHKey): Result[BigDecimal, (String, String)] =
    WS.url(baseUri + "account/keys")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(sshKey toJson)
      .map {
      case result@play.api.mvc.Results.Created => Left(Success((result.json \ "ssh_key" \ "id").as[BigDecimal]))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  /** Powers the droplet on. */
  def powerOnDroplet(id: String, apiKey: String): Result[(String, String), (String, String)] =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_on"))
      .map {
      case play.api.mvc.Results.Created => Left(Success("success" -> "Server powered on"))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  /** Powers the droplet off. */
  def powerOffDroplet(id: String, apiKey: String): Result[(String, String), (String, String)] =
    WS.url(baseUri + s"droplets/$id/actions")
      .withHeaders("Authorization" -> s"Bearer $apiKey")
      .post(Json.obj("type" -> "power_off"))
      .map {
      case play.api.mvc.Results.Created => Left(Success("success" -> "Server powered off"))
      case error => Right(Error("error" -> (error.json \ "message")
        .asOpt[String].getOrElse(error.json.toString)))
    }

  /** Deletes the droplet. */
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
