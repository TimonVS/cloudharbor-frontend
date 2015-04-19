package models

import scalikejdbc._

case class CloudService(
  id: Int,
  userId: Int,
  apiKey: String) {

  def save()(implicit session: DBSession = CloudService.autoSession): CloudService = CloudService.save(this)(session)

  def destroy()(implicit session: DBSession = CloudService.autoSession): Unit = CloudService.destroy(this)(session)

}


object CloudService extends SQLSyntaxSupport[CloudService] {

  override val tableName = "cloud_service"

  override val columns = Seq("id", "user_id", "api_key")

  def apply(cs: SyntaxProvider[CloudService])(rs: WrappedResultSet): CloudService = apply(cs.resultName)(rs)
  def apply(cs: ResultName[CloudService])(rs: WrappedResultSet): CloudService = new CloudService(
    id = rs.get(cs.id),
    userId = rs.get(cs.userId),
    apiKey = rs.get(cs.apiKey)
  )

  val cs = CloudService.syntax("cs")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[CloudService] = {
    withSQL {
      select.from(CloudService as cs).where.eq(cs.id, id)
    }.map(CloudService(cs.resultName)).single.apply()
  }

  def findByUserId(userId: Int)(implicit session: DBSession = autoSession): Option[CloudService] = {
    withSQL{
      select.from(CloudService as cs).where.eq(cs.userId, userId)
    }.map(CloudService(cs.resultName)).single().apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[CloudService] = {
    withSQL(select.from(CloudService as cs)).map(CloudService(cs.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(CloudService as cs)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[CloudService] = {
    withSQL {
      select.from(CloudService as cs).where.append(sqls"${where}")
    }.map(CloudService(cs.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[CloudService] = {
    withSQL {
      select.from(CloudService as cs).where.append(sqls"${where}")
    }.map(CloudService(cs.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(CloudService as cs).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
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
