package models

import scalikejdbc._
import org.mindrot.jbcrypt.BCrypt

case class User(
  id: Int,
  username: String,
  password: String,
  email: String,
  firstName: String,
  prefix: Option[String] = None,
  lastName: String) {
  def update(data: UserRegistrationData): User = {
    val dataPassword = data.password.getOrElse(this.password)
    this.copy(
      username = data.username,
      password = BCrypt.hashpw(dataPassword, BCrypt.gensalt(12)),
      email = data.email,
      firstName = data.firstName,
      prefix = data.prefix,
      lastName = data.lastName
    )
  }


  def save()(implicit session: DBSession = User.autoSession): User = User.save(this)(session)

  def destroy()(implicit session: DBSession = User.autoSession): Unit = User.destroy(this)(session)

}


object User extends SQLSyntaxSupport[User] {

  override val tableName = "users"

  override val columns = Seq("id", "username", "password", "email", "first_name", "prefix", "last_name")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = new User(
    id = rs.get(u.id),
    username = rs.get(u.username),
    password = rs.get(u.password),
    email = rs.get(u.email),
    firstName = rs.get(u.firstName),
    prefix = rs.get(u.prefix),
    lastName = rs.get(u.lastName)
  )

  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.id, id)
    }.map(User(u.resultName)).single.apply()
  }

  def findByUsername(username: String)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.username, username)
    }.map(User(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[User] = {
    withSQL(select.from(User as u)).map(User(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(User as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.append(sqls"${where}")
    }.map(User(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[User] = {
    withSQL {
      select.from(User as u).where.append(sqls"${where}")
    }.map(User(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(User as u).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
  }

  def create(
    username: String,
    password: String,
    email: String,
    firstName: String,
    prefix: Option[String] = None,
    lastName: String)(implicit session: DBSession = autoSession): User = {
    val generatedKey = withSQL {
      insert.into(User).columns(
        column.username,
        column.password,
        column.email,
        column.firstName,
        column.prefix,
        column.lastName
      ).values(
        username,
        BCrypt.hashpw(password, BCrypt.gensalt(12)),
        email,
        firstName,
        prefix,
        lastName
      )
    }.updateAndReturnGeneratedKey.apply()

    User(
      id = generatedKey.toInt, 
      username = username,
      password = password,
      email = email,
      firstName = firstName,
      prefix = prefix,
      lastName = lastName)
  }

  def save(entity: User)(implicit session: DBSession = autoSession): User = {
    withSQL {
      update(User).set(
        column.id -> entity.id,
        column.username -> entity.username,
        column.password -> entity.password,
        column.email -> entity.email,
        column.firstName -> entity.firstName,
        column.prefix -> entity.prefix,
        column.lastName -> entity.lastName
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: User)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(User).where.eq(column.id, entity.id) }.update.apply()
  }

}
