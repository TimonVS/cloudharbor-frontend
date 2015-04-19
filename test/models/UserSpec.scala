package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class UserSpec extends Specification {

  "User" should {

    val u = User.syntax("u")

    "find by primary keys" in new AutoRollback {
      val maybeFound = User.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = User.findBy(sqls.eq(u.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = User.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = User.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = User.findAllBy(sqls.eq(u.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = User.countBy(sqls.eq(u.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = User.create(username = "MyString", password = "MyString", email = "MyString", firstName = "MyString", lastName = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = User.findAll().head
      // TODO modify something
      val modified = entity
      val updated = User.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = User.findAll().head
      User.destroy(entity)
      val shouldBeNone = User.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
