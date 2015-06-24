package controllers

import play.api.mvc.{Result, AnyContent, Request}
import models._

import scala.concurrent.Future

/**
 * Created by ThomasWorkBook on 24/06/15.
 */
trait Messenger {

  def cloudInfo(apiKey: String): (String, String) = {
    "Cloud-Info" -> apiKey
  }

  def ping(implicit user: User, request: Request[AnyContent]): Future[Result]

  def servers(cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]

  def show(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]

  def powerOff(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]

  def powerOn(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]

  def delete(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]

  def createServer(cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]

  def getServerOptions(cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]

  def getSSHKeys(cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]

  def reboot(serverId: String, cloudService: ServerSettings)(implicit user: User, request: Request[AnyContent]): Future[Result]
}
