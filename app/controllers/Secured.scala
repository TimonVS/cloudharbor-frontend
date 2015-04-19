package controllers

import models.User
import play.api.mvc._

/**
 * Created by ThomasWorkBook on 16/04/15.
 * Trait for action composition for authenticated controllers
 */
trait Secured {

  def user(request: RequestHeader) =
    request
      .session
      .get(Security.username)
      .flatMap(username => User.findByUsername(username))

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Auth.login)

  def withAuth(f: => User => Request[AnyContent] => Result) = {
    Security.Authenticated(user, onUnauthorized) {user =>
      Action(request => f(user)(request))
    }
  }

}
