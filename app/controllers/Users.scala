package controllers

import models.{User, UserRegistrationData}
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.Controller

/**
 * Created by ThomasWorkBook on 21/04/15.
 */
object Users extends Controller with Secured{
  def createUserForm(passwordNeeded: Boolean) = Form(
    mapping(
      "username" -> nonEmptyText(minLength = 4),
      "password" -> optional(text(minLength = 4)),
      "passwordTwo" -> optional(text(minLength = 4)),
      "email" -> email,
      "firstName" -> nonEmptyText,
      "prefix" -> optional(text),
      "lastName" -> nonEmptyText
    )(UserRegistrationData.apply)(UserRegistrationData.unapply) verifying (
      "Make sure that the passwords are filled in correctly.",
      data => passwordCheck(passwordNeeded, data)
      )
  )

  def passwordCheck(passwordNeeded: Boolean, data: UserRegistrationData): Boolean =
    if (passwordNeeded)
      data.password.isDefined && data.passwordTwo.isDefined && data.password == data.passwordTwo
    else if(data.password.isDefined || data.passwordTwo.isDefined)
      data.password.isDefined && data.passwordTwo.isDefined && data.password == data.passwordTwo
    else
      true


  def createUser = withAuth { implicit user => implicit request =>
    Ok(views.html.user.create(createUserForm(true)))
  }

  def createUserPost = withAuth { implicit user => implicit request =>
    createUserForm(true).bindFromRequest().fold(
      formWithErrors => BadRequest(views.html.user.create(formWithErrors)),
      data => {
        val user = User.create(
          data.username,
          data.password.get,
          data.email,
          data.firstName,
          data.prefix,
          data.lastName
        )
        Redirect(routes.Users.createUser()).flashing("succes" -> s"${user.username} was created with an id: ${user.id}")
      }
    )
  }

  def showUser = withAuth { implicit user => implicit request =>
    val filledInForm = createUserForm(false).fill(UserRegistrationData(user))
    Ok(views.html.user.profile(filledInForm))
  }

  def updateUser = withAuth { implicit user => implicit request =>
    createUserForm(false).bindFromRequest().fold(
      formWithErrors => Ok(views.html.user.profile(formWithErrors)),
      data => {
        user.update(data).save()
        Redirect(routes.Users.showUser()).flashing("succes" -> "User profile was updated succesfully.")
      }
    )
  }

}
