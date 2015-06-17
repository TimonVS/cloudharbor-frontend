package models

import scalikejdbc._

case class ServerSettings(
  id: Int,
  userId: Int,
  apiKey: String) {

  def save()(implicit session: DBSession = ServerSettings.autoSession): ServerSettings = ServerSettings.save(this)(session)

  def destroy()(implicit session: DBSession = ServerSettings.autoSession): Unit = ServerSettings.destroy(this)(session)

}


object ServerSettings extends SQLSyntaxSupport[ServerSettings] with StandardQueries[ServerSettings]{

  override val tableName = "cloud_service"

  override val columns = Seq("id", "user_id", "api_key")

  override val autoSession = AutoSession

  override val sp = ServerSettings.syntax("cs")

  override val aliasSyntax = ServerSettings as sp

  override val result = ServerSettings(sp.resultName) _

  def apply(cs: SyntaxProvider[ServerSettings])(rs: WrappedResultSet): ServerSettings = apply(cs.resultName)(rs)
  def apply(cs: ResultName[ServerSettings])(rs: WrappedResultSet): ServerSettings = new ServerSettings(
    id = rs.get(cs.id),
    userId = rs.get(cs.userId),
    apiKey = rs.get(cs.apiKey)
  )

  def findByUserId(userId: Int)(implicit session: DBSession = autoSession): Option[ServerSettings] = {
    withSQL{
      select.from(aliasSyntax).where.eq(sp.userId, userId)
    }.map(result).single().apply()
  }

  def create(
    userId: Int,
    apiKey: String)(implicit session: DBSession = autoSession): ServerSettings = {
    val generatedKey = withSQL {
      insert.into(ServerSettings).columns(
        column.userId,
        column.apiKey
      ).values(
        userId,
        apiKey
      )
    }.updateAndReturnGeneratedKey.apply()

    ServerSettings(
      id = generatedKey.toInt,
      userId = userId,
      apiKey = apiKey)
  }

  def save(entity: ServerSettings)(implicit session: DBSession = autoSession): ServerSettings = {
    withSQL {
      update(ServerSettings).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.apiKey -> entity.apiKey
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: ServerSettings)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(ServerSettings).where.eq(column.id, entity.id) }.update.apply()
  }

}
