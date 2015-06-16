package controllers

import actors.NotificationActor.CreateServerNotification
import actors.RepeatActor.Repeat
import actors.{NotificationActor, RepeatActor}
import models.Notifications.ServerNotification
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Rudie on 16-6-2015.
 */
trait ServerManagementNotifications {

  private lazy val serverManagementUrl = current.configuration.getString("serverManagement.url").get + ":" + current.configuration.getInt("serverManagement.port").get
  private lazy val repeatActor = Akka.system.actorOf(RepeatActor.props)
  private lazy val notificationActor = Akka.system.actorOf(NotificationActor.props)

  def notifyServerCreated(userId: Int, serverId: String, apiKey: String) = {
    def repeat() = WS.url(s"http://$serverManagementUrl/servers/$serverId")
      .withHeaders("Cloud-Info" -> apiKey)
      .get()
      .map(response => (response.json \ "status").as[String] == "active")

    def done() = notificationActor ! CreateServerNotification(userId, ServerNotification(serverId, "Server created"))

    repeatActor ! Repeat(repeat, done)
  }
}
