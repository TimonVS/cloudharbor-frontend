package models

import org.joda.time.DateTime
import scalikejdbc._

/**
 * Created by ThomasWorkBook on 02/05/15.
 */
case class Notification(
                       id: Int,
                       userId: Int,
                       message: String,
                       timeStamp: DateTime,
                       notificationType: String,
                       read: Boolean) {

  def save()(implicit session: DBSession = Notification.autoSession): Notification = Notification.save(this)(session)

  def destroy()(implicit session: DBSession = Notification.autoSession): Unit = Notification.destroy(this)(session)

}

object NotificationType extends Enumeration {
  type NotificationType = Value
  val ServerCreate, ServerDelete = Value
}

object NotificationMessages {
  def serverCreated(id: BigDecimal) = s"You created a server with the id of $id"

  def serverDeleted(id: BigDecimal) = s"You deleted a server with the id of $id"
}

object Notification extends SQLSyntaxSupport[Notification] with StandardQueries[Notification]{

  override val tableName = "notification"

  override val columns = Seq("id", "user_id", "message", "time_stamp", "type", "read")

  override val nameConverters = Map("^notificationType$" -> "type")

  override val sp = Notification.syntax("n")

  override val autoSession = AutoSession

  override val aliasSyntax = Notification as sp

  override val result: (WrappedResultSet) => Notification = Notification(sp.resultName) _

  def apply(cs: SyntaxProvider[Notification])(rs: WrappedResultSet): Notification = apply(cs.resultName)(rs)
  def apply(cs: ResultName[Notification])(rs: WrappedResultSet): Notification = new Notification(
    id = rs.get(cs.id),
    userId = rs.get(cs.userId),
    message = rs.get(cs.message),
    timeStamp = rs.get(cs.timeStamp),
    notificationType = rs.get(cs.notificationType),
    read = rs.get(cs.read)
  )

  def findByUserId(userId: Int)(implicit session: DBSession = autoSession): List[Notification] = {
    withSQL{
      select.from(aliasSyntax).where.eq(sp.userId, userId)
    }.map(result).list().apply()
  }

  def create(
              userId: Int,
              message: String,
              timeStamp: DateTime,
              notificationType: String,
              read: Boolean = false)(implicit session: DBSession = autoSession): Notification = {
    val generatedKey = withSQL {
      insert.into(Notification).columns(
        column.userId,
        column.message,
        column.timeStamp,
        column.notificationType,
        column.read
      ).values(
          userId,
          message,
          timeStamp,
          notificationType,
          read
        )
    }.updateAndReturnGeneratedKey.apply()

    Notification(
      id = generatedKey.toInt,
      userId = userId,
      message = message,
      timeStamp = timeStamp,
      notificationType = notificationType,
      read = read
    )
  }

  def save(entity: Notification)(implicit session: DBSession = autoSession): Notification = {
    withSQL {
      update(CloudService).set(
        column.id -> entity.id,
        column.message -> entity.message,
        column.timeStamp -> entity.timeStamp,
        column.notificationType -> entity.notificationType,
        column.read -> entity.read
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Notification)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(CloudService).where.eq(column.id, entity.id) }.update.apply()
  }

}
