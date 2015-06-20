package utils

import controllers.routes
import models.User
import play.api.mvc._

import scala.concurrent.Future

/**
 * Created by ThomasWorkBook on 16/04/15.
 * Trait for action composition for authenticated controllers
 */
trait Secured extends WsUtils {

  def withAuth(f: => User => Request[AnyContent] => Result) = {
    Security.Authenticated(user, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }

  def forwardGetWithAuth[T](url: String, headerContent: T, createHeader: T => (String, String), service: String) =
    withAuthAsync { implicit user => implicit request => forwardGet(url, headerContent, createHeader, service) }

  def withAuthAsync(f: => User => Request[AnyContent] => Future[Result]) = {
    Security.Authenticated(user, onUnauthorized) { user =>
      Action.async(request => f(user)(request))
    }
  }

  def user(request: RequestHeader) =
    request
      .session
      .get(Security.username)
      .flatMap(username => User.findByUsername(username))

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Auth.login)

  def forwardPostWithAuth[T](url: String, headerContent: T, createHeader: T => (String, String), service: String) =
    withAuthAsync { implicit user => implicit request => forwardPost(url, headerContent, createHeader, service) }

  def forwardDeleteWithAuth[T](url: String, headerContent: T, createHeader: T => (String, String), service: String) =
    withAuthAsync { implicit user => implicit request => forwardDelete(url, headerContent, createHeader, service) }

}
