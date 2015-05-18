package models

import scalikejdbc._

case class Server(
  id: Int,
  userId: Int,
  apiKey: String) {

  def save()(implicit session: DBSession = Server.autoSession): Server = Server.save(this)(session)

  def destroy()(implicit session: DBSession = Server.autoSession): Unit = Server.destroy(this)(session)

}


object Server extends SQLSyntaxSupport[Server] with StandardQueries[Server]{

  override val tableName = "cloud_service"

  override val columns = Seq("id", "user_id", "api_key")

  override val autoSession = AutoSession

  override val sp = Server.syntax("cs")

  override val aliasSyntax = Server as sp

  override val result = Server(sp.resultName) _

  def apply(cs: SyntaxProvider[Server])(rs: WrappedResultSet): Server = apply(cs.resultName)(rs)
  def apply(cs: ResultName[Server])(rs: WrappedResultSet): Server = new Server(
    id = rs.get(cs.id),
    userId = rs.get(cs.userId),
    apiKey = rs.get(cs.apiKey)
  )

  def findByUserId(userId: Int)(implicit session: DBSession = autoSession): Option[Server] = {
    withSQL{
      select.from(aliasSyntax).where.eq(sp.userId, userId)
    }.map(result).single().apply()
  }

  def create(
    userId: Int,
    apiKey: String)(implicit session: DBSession = autoSession): Server = {
    val generatedKey = withSQL {
      insert.into(Server).columns(
        column.userId,
        column.apiKey
      ).values(
        userId,
        apiKey
      )
    }.updateAndReturnGeneratedKey.apply()

    Server(
      id = generatedKey.toInt,
      userId = userId,
      apiKey = apiKey)
  }

  def save(entity: Server)(implicit session: DBSession = autoSession): Server = {
    withSQL {
      update(Server).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.apiKey -> entity.apiKey
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Server)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Server).where.eq(column.id, entity.id) }.update.apply()
  }

}
