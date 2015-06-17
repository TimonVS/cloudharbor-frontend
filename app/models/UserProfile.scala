package models

import play.api.libs.json.Json

/**
 * Created by ThomasWorkBook on 17/06/15.
 */
case class UserProfile(
  username: String,
  password: Option[String],
  email: String,
  firstName: String,
  prefix: Option[String],
  lastName: String) {

  def save(user: User) = password.map { p =>
    user.copy(user.id, username, p, email, firstName, prefix, lastName).save()
  } getOrElse user.copy(user.id, username, user.password, email, firstName, prefix, lastName).save()

  def create: Either[String, User] = password map { p =>
    Right(User.create(username, p, email, firstName, prefix, lastName).save())
  } getOrElse Left("Password needs to be specified!")

}

object UserProfile {

  implicit val userProfileWrites = Json.writes[UserProfile]

  implicit val userProfileReads = Json.reads[UserProfile]

}
