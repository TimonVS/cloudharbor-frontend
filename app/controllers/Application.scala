package controllers

import controllers.DockerContainerManagement._
import models.User
import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action { implicit request =>
    Redirect(routes.Auth.login())
  }

  def app(path: String) = withAuth { implicit user => implicit request =>
    Ok(views.html.app(request, user))
  }

}
