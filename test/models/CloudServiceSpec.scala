package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class CloudServiceSpec extends Specification {

  "Server" should {

    val cs = Server.syntax("cs")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Server.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Server.findBy(sqls.eq(cs.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Server.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Server.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Server.findAllBy(sqls.eq(cs.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Server.countBy(sqls.eq(cs.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Server.create(userId = 123, apiKey = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Server.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Server.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Server.findAll().head
      Server.destroy(entity)
      val shouldBeNone = Server.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
