package controllers.docker

import java.net.ConnectException

import controllers.docker.ImagesManagement._
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Api-gateway for all other calls than images and containers.
 *
 * Created by Rudie on 9-6-2015.
 */
object Management extends DockerManagement {

  def ping = withAuthAsync { implicit user => implicit request =>
    WS.url(s"http://$managementUrl/ping")
      .get()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => InternalServerError(unavailableJsonMessage("Docker Management"))
      case _ => InternalServerError(unexpectedError)
    }
  }

  /** Created by Thomas Meijers */
  def pingDockerRemoteApi(serverUrl: String) = withAuthAsync(implicit user => implicit request =>
    WS.url(s"http://$managementUrl/misc/ping")
      .withHeaders(dockerInfo(serverUrl))
      .get()
      .map(forwardResponse)
      .recover {
      case _: ConnectException => InternalServerError(unavailableJsonMessage("Docker Remote API"))
      case _ => InternalServerError(unexpectedError)
    }
  )
}
