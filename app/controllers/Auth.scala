package controllers

import models.{UserRegistrationData, User, UserData}
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{Security, Action, Controller}
import org.mindrot.jbcrypt.BCrypt

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

  val createUserForm = Form(
    mapping(
      "username" -> nonEmptyText(minLength = 4),
      "password" -> nonEmptyText(minLength = 4),
      "email" -> email,
      "firstName" -> nonEmptyText,
      "prefix" -> optional(text),
      "lastName" -> nonEmptyText
    )(UserRegistrationData.apply)(UserRegistrationData.unapply)
  )

  def check(username: String, password: String): Boolean = {
    val userOpt = User.findByUsername(username)
    userOpt map(data => BCrypt.checkpw(password, data.password)) getOrElse false
  }

  def login = Action { implicit request =>
    if(request.session.get("username").isDefined) Redirect(routes.Management.overview())
    else Ok(views.html.user.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.user.login(formWithErrors)),
      user => Redirect(routes.Management.overview).withSession(Security.username -> user.username)
    )
  }

  def logout = Action {
    Redirect(routes.Auth.login).withNewSession.flashing(
      "succes" -> "You are now logged out"
    )
  }

  def createUser = withAuth { username => implicit request =>
    Ok(views.html.user.create(createUserForm))
  }

  def createUserPost = withAuth { username => implicit request =>
    createUserForm.bindFromRequest().fold(
      formWithErrors => BadRequest(views.html.user.create(formWithErrors)),
      data => {
        val user = User.create(
          data.username,
          data.password,
          data.email,
          data.firstName,
          data.prefix,
          data.lastName
        )
        Redirect(routes.Auth.createUser()).flashing("succes" -> s"${user.username} is created with an id: ${user.id}")
      }
    )
  }

}
