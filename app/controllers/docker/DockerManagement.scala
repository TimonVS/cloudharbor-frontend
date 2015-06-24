package controllers.docker

import play.api.Play._
import play.api.mvc.Controller

/**
 * Provides useful functions for all Docker related controllers.
 *
 * Created by Rudie on 4-6-2015.
 */
trait DockerManagement extends Controller {

  /** Created by Thomas Meijers */
  lazy val managementUrl = current.configuration.getString("management.url").get + ":" + current.configuration.getInt("management.port").get

  /** Created by Thomas Meijers */
  def dockerInfo(serverUrl: String): (String, String) = "Docker-Info" -> s"$serverUrl:4243"
}
