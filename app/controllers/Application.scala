package controllers

import controllers.docker.ContainerManagement
import controllers.docker.ContainerManagement._
import play.api.mvc._

object Application extends Controller {

  def index = Action { implicit request =>
    Redirect(routes.Auth.login())
  }

  def app(path: String) = withAuth { implicit user => implicit request =>
    Ok(views.html.app(request, user))
  }

}
