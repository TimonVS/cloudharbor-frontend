package controllers

import play.api.mvc.Controller

/**
 * Created by ThomasWorkBook on 17/04/15.
 * Link Api for management of docker containers
 */
object Management extends Controller with Secured{

  def overview = withAuth{ user => implicit request =>
    Ok(views.html.management.overview())
  }

}