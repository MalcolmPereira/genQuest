package dao

import org.scalatest._
import model.User


class UserDAOXMLImplSpec extends FlatSpec {

  "UserDAO " should " Return all users " in {
    assert(UserDAOXMLImpl.listUsers != null)
    assert(UserDAOXMLImpl.listUsers.size > 0 )
  }

  "UserDAO " should " Return valid user for username/password " in {
    val user = UserDAOXMLImpl.findUser("malcolm","malcolm")
    assert(user != null)
    assert(user.name == "malcolm")
  }

  "UserDAO " should " Return null user for invalid username/password " in {
    assert(UserDAOXMLImpl.findUser("invalid","invalid") == null)
  }

  "UserDAO " should " Return valid user for username " in {
    val user = UserDAOXMLImpl.findUser("malcolm")
    assert(user != null)
    assert(user.name == "malcolm")
  }

  "UserDAO " should " Return null for invalid username " in {
    assert(UserDAOXMLImpl.findUser("invalid") == null)
  }

  "UserDAO " should " Return valid user for userid " in {
    val user = UserDAOXMLImpl.findUser(10001)
    assert(user != null)
    assert(user.name == "malcolm")
  }

  "UserDAO " should " Return null for invalid userid " in {
    assert(UserDAOXMLImpl.findUser(0) == null)
  }

  "UserDAO " should " add new user " in {
    assert(UserDAOXMLImpl.addUser(new User(0,"newuser","newuser","newuser firstname", "new user lastname")) > 0)
  }

}
