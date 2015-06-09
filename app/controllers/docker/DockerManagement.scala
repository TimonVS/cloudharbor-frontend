package controllers.docker

import play.api.Play._
import play.api.mvc.Controller

/**
 * Created by Rudie on 4-6-2015.
 */
trait DockerManagement extends Controller {

  lazy val managementUrl = current.configuration.getString("management.url").get + ":" + current.configuration.getInt("management.port").get

  def dockerInfo(serverUrl: String): (String, String) = "Docker-Info" -> s"$serverUrl:4243"
}
