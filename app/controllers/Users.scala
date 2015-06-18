package controllers

import models.Notifications.{DBNotification, Notification}
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.Controller
import scala.concurrent.Future

/**
 * Created by ThomasWorkBook on 21/04/15.
 */
object Users extends Controller with Secured{

  // updated user with json
  def getUser = withAuthAsync{ implicit user => implicit request =>
    Future.successful(Ok(Json.toJson(user.toUserProfile)))
  }

  def saveUser = withAuthAsync { implicit user => implicit request =>
    request.body.asJson map { json =>
      json.validate[UserProfile].fold(
        error => Future.successful(BadRequest(Json.obj("error" -> "Json was not correct!"))),
        userProfile =>  {
          userProfile.save(user)
          Future.successful(NoContent.withHeaders(LOCATION -> routes.Users.getUser.absoluteURL()))
        }
      )
    } getOrElse Future.successful(BadRequest(Json.obj("error" -> "Expecting application/json!")))
  }

  def createUser = withAuthAsync { implicit user => implicit request =>
    request.body.asJson map { json =>
      json.validate[UserProfile].fold(
        error => Future.successful(BadRequest(Json.obj("error" -> "Json was not correct!"))),
        userProfile =>  {
          val user = userProfile.create
          user.fold(
            error => Future.successful(BadRequest(Json.obj("error" -> error))),
            user => Future.successful(NoContent)
          )
        }
      )
    } getOrElse Future.successful(BadRequest(Json.obj("error" -> "Expecting application/json!")))
  }

  def deleteUser(userId: Option[Int] = None) = withAuthAsync{ implicit user => implicit request =>
    Future.successful{
      userId map {id =>
        User.find(id) match {
          case Some(u) =>
            DBNotification.findByUserId(u.id) foreach { n => n.destroy()}
            ServerSettings.findByUserId(u.id) map(_.destroy())
            u.destroy()
            NoContent
          case None => NotFound(Json.obj("error" -> "The user you tried to delete was not found!"))
        }
      } getOrElse{
          DBNotification.findByUserId(user.id) foreach { n => n.destroy()}
          ServerSettings.findByUserId(user.id) map(_.destroy())
          user.destroy()
          NoContent
      }
    }
  }

  def logout = withAuthAsync { implicit user => implicit request =>
    Future.successful(Ok(Json.obj("success" -> "Logout succesfull")).withNewSession)
  }

}
