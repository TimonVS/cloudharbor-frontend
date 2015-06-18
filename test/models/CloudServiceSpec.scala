package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class CloudServiceSpec extends Specification {

  "Server" should {

    val cs = ServerSettings.syntax("cs")

    "find by primary keys" in new AutoRollback {
      val maybeFound = ServerSettings.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = ServerSettings.findBy(sqls.eq(cs.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = ServerSettings.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = ServerSettings.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = ServerSettings.findAllBy(sqls.eq(cs.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = ServerSettings.countBy(sqls.eq(cs.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = ServerSettings.create(userId = 123, apiKey = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = ServerSettings.findAll().head
      // TODO modify something
      val modified = entity
      val updated = ServerSettings.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = ServerSettings.findAll().head
      ServerSettings.destroy(entity)
      val shouldBeNone = ServerSettings.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
