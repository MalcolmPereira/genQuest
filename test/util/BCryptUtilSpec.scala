package util

import org.scalatest._

class BCryptUtilSpec extends FlatSpec {
  "BCryptUtil " should "Encrypt string correctly" in {
    val encypted = BCryptUtil.create("malcolm")
    assert(encypted != null)
    assert(encypted.trim.length != 0)
  }

  "BCryptUtil " should "Decrypt string and check correctly" in {
    val encypted = BCryptUtil.create("malcolm")
    assert(encypted != null)
    assert(encypted.trim.length != 0)
    assert(BCryptUtil.check("malcolm","$2a$10$NeNjyd5AFIIsEB9VdWTp0Op339ewJFdh55EZ/0xFtupdm0r6ahO0q"))
  }
}
