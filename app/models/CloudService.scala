package models

import scalikejdbc._

case class CloudService(
  id: Int,
  userId: Int,
  apiKey: String) {

  def save()(implicit session: DBSession = CloudService.autoSession): CloudService = CloudService.save(this)(session)

  def destroy()(implicit session: DBSession = CloudService.autoSession): Unit = CloudService.destroy(this)(session)

}


object CloudService extends SQLSyntaxSupport[CloudService] with StandardQueries[CloudService]{

  override val tableName = "cloud_service"

  override val columns = Seq("id", "user_id", "api_key")

  override val autoSession = AutoSession

  override val sp = CloudService.syntax("cs")

  override val aliasSyntax = CloudService as sp

  override val result = CloudService(sp.resultName) _

  def apply(cs: SyntaxProvider[CloudService])(rs: WrappedResultSet): CloudService = apply(cs.resultName)(rs)
  def apply(cs: ResultName[CloudService])(rs: WrappedResultSet): CloudService = new CloudService(
    id = rs.get(cs.id),
    userId = rs.get(cs.userId),
    apiKey = rs.get(cs.apiKey)
  )

  def findByUserId(userId: Int)(implicit session: DBSession = autoSession): Option[CloudService] = {
    withSQL{
      select.from(aliasSyntax).where.eq(sp.userId, userId)
    }.map(result).single().apply()
  }

  def create(
    userId: Int,
    apiKey: String)(implicit session: DBSession = autoSession): CloudService = {
    val generatedKey = withSQL {
      insert.into(CloudService).columns(
        column.userId,
        column.apiKey
      ).values(
        userId,
        apiKey
      )
    }.updateAndReturnGeneratedKey.apply()

    CloudService(
      id = generatedKey.toInt, 
      userId = userId,
      apiKey = apiKey)
  }

  def save(entity: CloudService)(implicit session: DBSession = autoSession): CloudService = {
    withSQL {
      update(CloudService).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.apiKey -> entity.apiKey
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: CloudService)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(CloudService).where.eq(column.id, entity.id) }.update.apply()
  }

}
