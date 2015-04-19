package controllers

import models.User
import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action { implicit request =>
    Redirect(routes.Auth.login())
  }

}