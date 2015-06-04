package controllers

import play.api.libs.EventSource
import play.api.libs.iteratee.Concurrent
import play.api.mvc.Controller

/**
 * Created by Rudie on 4-6-2015.
 */
object Notifications extends Controller with Secured {

  lazy val (notificationsOut, notificationsIn) = Concurrent.broadcast[String]

  def notifications = withAuth { implicit user => implicit request =>
    Ok.feed(notificationsOut &> EventSource()).as("text/event-stream")
  }
}
