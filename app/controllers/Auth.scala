package controllers

import models.{User, UserData}
import org.mindrot.jbcrypt.BCrypt
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{Action, Controller, Security}

/**
 * Created by ThomasWorkBook on 16/04/15.
 * Controller for authentication related actions
 */
object Auth extends Controller with Secured{

  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserData.apply)(UserData.unapply) verifying (
        "Username or password weren't correct",
        userData => check(userData.username, userData.password)
      )
  )

  def check(username: String, password: String): Boolean = {
    val userOpt = User.findByUsername(username)
    userOpt map(data => BCrypt.checkpw(password, data.password)) getOrElse false
  }

  def login = Action { implicit request =>
    if (request.session.get("username").isDefined) Redirect(routes.Application.app("dashboard")) //DockerContainerManagement.overview()
    else Ok(views.html.user.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.user.login(formWithErrors)),
      user => Redirect(routes.DockerContainerManagement.overview).withSession(Security.username -> user.username)
    )
  }

  def logout = Action {
    Redirect(routes.Auth.login).withNewSession.flashing(
      "succes" -> "You are now logged out"
    )
  }



}
