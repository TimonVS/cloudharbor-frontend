package models

import play.api.mvc.Flash

/**
 * Created by Rudie on 3-5-2015.
 */

object SuccessFlash {
  def apply(message: String) = SuccessFlash(Map("success" -> message))
}

case class SuccessFlash(override val data: Map[String, String]) extends Flash

object ErrorFlash {
  def apply(message: String) = ErrorFlash(Map("error" -> message))
}

case class ErrorFlash(override val data: Map[String, String]) extends Flash
