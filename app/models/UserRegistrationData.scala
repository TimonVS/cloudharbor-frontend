package models

/**
 * Created by ThomasWorkBook on 19/04/15.
 * case class for holding the user registration form data
 */
case class UserRegistrationData(username: String,
                                password: Option[String],
                                passwordTwo: Option[String],
                                email: String,
                                firstName: String,
                                prefix: Option[String],
                                lastName: String)

object UserRegistrationData{

  def apply(user: User): UserRegistrationData = UserRegistrationData(
    user.username,
    None,
    None,
    user.email,
    user.firstName,
    user.prefix,
    user.lastName
  )

}

