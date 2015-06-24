package controllers

import models.Notifications.Notification
import play.api.libs.EventSource
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.Json
import play.api.mvc.Controller
import utils.Secured

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Controller for notification endpoints.
 *
 * Created by Rudie on 4-6-2015.
 */
object Notifications extends Controller with Secured {

  lazy val (notificationsOut, notificationsIn) = Concurrent.broadcast[Notification]

  /** forwards all notification as a json object in SSE */
  def notifications = withAuth { implicit user => implicit request =>
    Ok.feed(notificationsOut.map(Json.toJson(_)) &> EventSource()).as("text/event-stream")
  }
}
