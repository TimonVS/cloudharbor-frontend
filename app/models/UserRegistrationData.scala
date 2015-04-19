package models

/**
 * Created by ThomasWorkBook on 19/04/15.
 * case class for holding the user registration form data
 */
case class UserRegistrationData(username: String,
                                password: String,
                                email: String,
                                firstName: String,
                                prefix: Option[String],
                                lastName: String)

