package api

import models.DigitalOcean.{Droplet, SSHKey}
import models.{CloudServer, Error, Success}

import scala.concurrent.Future
import scala.util.Either

/**
 * Created by Rudie on 3-5-2015.
 */
trait CloudAPI {
  type Result[A, B] = Future[Either[Success[A], Error[B]]]

  /** Authenticate to the API. */
  def authenticate(apiKey: String): Result[(String, String), (String, String)]

  /** Retrieves a server. */
  def getCloudServer(id: BigDecimal, apiKey: String): Result[CloudServer, (String, String)]

  /** Retrieves a list of all servers */
  def getCloudServers(apiKey: String): Result[List[CloudServer], (String, String)]

  /** Creates a service. */
  def createService(apiKey: String, droplet: Droplet): Result[(String, String), (String, String)]

  /** Adds a SSH key to the API account. */
  def addSSHKey(apiKey: String, sshKey: SSHKey): Result[BigDecimal, (String, String)]

  /** Powers the service on. */
  def powerOn(id: String, apiKey: String): Result[(String, String), (String, String)]

  /** Powers the service off. */
  def powerOff(id: String, apiKey: String): Result[(String, String), (String, String)]

  /** Deletes the service. */
  def delete(id: String, apiKey: String): Result[(String, String), (String, String)]
}
