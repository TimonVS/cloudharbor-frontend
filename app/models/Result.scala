package models

/**
 * Created by Rudie on 3-5-2015.
 */
case class Success[T](data: T)

case class Error[T](data: T)
