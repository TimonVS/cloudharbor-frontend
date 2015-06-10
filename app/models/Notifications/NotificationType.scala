package models.Notifications

/**
 * Created by Rudie on 10-6-2015.
 */
object NotificationType extends Enumeration {
  type NotificationType = Value
  val ServerCreate, ServerDelete, ContainerCreate, ContainerDelete, ImageCreate, ImageDelete = Value
}
