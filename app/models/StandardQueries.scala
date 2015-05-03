package models
import scalikejdbc._

/**
 * Created by ThomasWorkBook on 03/05/15.
 */
trait StandardQueries[T] {

  def sp: QuerySQLSyntaxProvider[SQLSyntaxSupport[T], T]

  def autoSession: DBSession

  def aliasSyntax: TableAsAliasSQLSyntax

  def result: (WrappedResultSet) => T

  def find(id: Int)(implicit session: DBSession = autoSession): Option[T] = {
    withSQL {
      select.from(aliasSyntax).where.eq(sp.id, id)
    }.map(result).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[T] = {
    withSQL(select.from(aliasSyntax)).map(result).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(aliasSyntax)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[T] = {
    withSQL {
      select.from(aliasSyntax).where.append(sqls"${where}")
    }.map(result).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[T] = {
    withSQL {
      select.from(aliasSyntax).where.append(sqls"${where}")
    }.map(result).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(aliasSyntax).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
  }

}
