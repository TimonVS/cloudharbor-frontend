package controllers.docker

import java.net.ConnectException

import controllers.docker.ImagesManagement._
import play.api.Play.current
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Rudie on 8-6-2015.
 */
object DeployManagement extends DockerManagement {

  val DEPLOY_MANAGEMENT = "Deploy Management"

  def deploy(serverUrl: String, imageName: String) = withAuthAsync { implicit user => implicit request =>
    WS.url(s"http://$managementUrl/deploy/$imageName")
      .withHeaders(dockerInfo(serverUrl))
      .post(request.body.asJson.get)
      .map(forwardResponse)
      .recover {
      case _: ConnectException => InternalServerError(unavailableJsonMessage(DEPLOY_MANAGEMENT))
      case _ => InternalServerError(unexpectedError)
    }
  }
}
