package models.Notifications

import models.Notifications.NotificationType.NotificationType
import models.StandardQueries
import org.joda.time.DateTime
import play.api.libs.json.JsValue
import scalikejdbc._

/**
 * Created by ThomasWorkBook on 02/05/15.
 */
case class DBNotification(
                       id: Int,
                       userId: Int,
                       message: String,
                       timeStamp: DateTime,
                       notificationType: String,
                       read: Boolean) {

  def save()(implicit session: DBSession = DBNotification.autoSession): DBNotification = DBNotification.save(this)(session)

  def destroy()(implicit session: DBSession = DBNotification.autoSession): Unit = DBNotification.destroy(this)(session)

}


object DBNotification extends SQLSyntaxSupport[DBNotification] with StandardQueries[DBNotification] {

  override val tableName = "notification"

  override val columns = Seq("id", "user_id", "message", "time_stamp", "type", "read")

  override val nameConverters = Map("^notificationType$" -> "type")

  override val sp = DBNotification.syntax("n")

  override val autoSession = AutoSession

  override val aliasSyntax = DBNotification as sp

  override val result: (WrappedResultSet) => DBNotification = DBNotification(sp.resultName) _

  def apply(cs: SyntaxProvider[DBNotification])(rs: WrappedResultSet): DBNotification = apply(cs.resultName)(rs)

  def apply(cs: ResultName[DBNotification])(rs: WrappedResultSet): DBNotification = new DBNotification(
    id = rs.get(cs.id),
    userId = rs.get(cs.userId),
    message = rs.get(cs.message),
    timeStamp = rs.get(cs.timeStamp),
    notificationType = rs.get(cs.notificationType),
    read = rs.get(cs.read)
  )

  def findByUserId(userId: Int)(implicit session: DBSession = autoSession): List[DBNotification] = {
    withSQL{
      select.from(aliasSyntax).where.eq(sp.userId, userId)
    }.map(result).list().apply()
  }

  /** Created by Rudie de Smit */
  def create(userId: Int, message: JsValue, notificationType: NotificationType): DBNotification =
    create(userId, message.toString, DateTime.now, notificationType.toString)

  def create(
              userId: Int,
              message: String,
              timeStamp: DateTime,
              notificationType: String,
              read: Boolean = false)(implicit session: DBSession = autoSession): DBNotification = {
    val generatedKey = withSQL {
      insert.into(DBNotification).columns(
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

    DBNotification(
      id = generatedKey.toInt,
      userId = userId,
      message = message,
      timeStamp = timeStamp,
      notificationType = notificationType,
      read = read
    )
  }

  def save(entity: DBNotification)(implicit session: DBSession = autoSession): DBNotification = {
    withSQL {
      update(DBNotification).set(
        column.id -> entity.id,
        column.message -> entity.message,
        column.timeStamp -> entity.timeStamp,
        column.notificationType -> entity.notificationType,
        column.read -> entity.read
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: DBNotification)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(DBNotification).where.eq(column.id, entity.id) }.update.apply()
  }

}
