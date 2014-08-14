package dao.file

import model.User
import org.scalatest._
import org.specs2.mutable.Specification
import play.api.test.WithApplication


class UserDAOXMLImplSpec extends Specification {

  "UserDAO " should {
    " Return all users " in new WithApplication {
      val users = UserDAOXMLImpl.listUsers
      assert(users != null)
      assert(users.size > 0)
      for(user <- users){
        assert(user.id != null)
        assert(user.id > 0)
        assert(user.name != null)
        assert(user.name.length > 0)
        assert(user.password != null)
        assert(user.password.length == 0)
        assert(user.firstName != null)
        assert(user.firstName.length > 0)
        assert(user.lastName != null)
        assert(user.lastName.length > 0)
      }
    }
  }

  "UserDAO " should {
    " Return valid user for username/password " in new WithApplication {
      val user = UserDAOXMLImpl.findUser("malcolm", "malcolm")
      assert(user != null)
      assert(user.name == "malcolm")
      assert(user.id != null)
      assert(user.id > 0)
      assert(user.password != null)
      assert(user.password.length == 0)
      assert(user.firstName != null)
      assert(user.firstName.length > 0)
      assert(user.lastName != null)
      assert(user.lastName.length > 0)
    }
  }

  "UserDAO " should {
    " Return null user for invalid username/password " in new WithApplication {
      assert(UserDAOXMLImpl.findUser("invalid", "invalid") == null)
    }
  }

  "UserDAO " should {
     " Return valid user for username " in new WithApplication {
      val user = UserDAOXMLImpl.findUser("malcolm")
      assert(user != null)
      assert(user.name == "malcolm")
      assert(user.id != null)
      assert(user.id > 0)
      assert(user.password != null)
      assert(user.password.length == 0)
      assert(user.firstName != null)
      assert(user.firstName.length > 0)
      assert(user.lastName != null)
      assert(user.lastName.length > 0)
    }
  }

  "UserDAO " should {
     " Return null for invalid username " in new WithApplication {
      assert(UserDAOXMLImpl.findUser("invalid") == null)
    }
  }

  "UserDAO " should  {
     " Return valid user for userid " in new WithApplication {
      val user = UserDAOXMLImpl.findUser(10001)
      assert(user != null)
      assert(user.name == "malcolm")
      assert(user.id != null)
      assert(user.id > 0)
      assert(user.password != null)
      assert(user.password.length == 0)
      assert(user.firstName != null)
      assert(user.firstName.length > 0)
      assert(user.lastName != null)
      assert(user.lastName.length > 0)
    }
  }

  "UserDAO " should {
     " Return null for invalid userid " in new WithApplication {
      assert(UserDAOXMLImpl.findUser(0) == null)
    }
  }

  "UserDAO " should {
     " add/update and delete new user " in new WithApplication {
      val userID = UserDAOXMLImpl.addUser(new User(0,"newuser","newuser","newuser firstname", "new user lastname"))
      assert( userID > 0)
      val user = UserDAOXMLImpl.updateUser(new User(userID,"","","updated user first name", "updated user first name"))
      assert( user != null)
      val rowid = UserDAOXMLImpl.deleteUser(user)
      assert( rowid != 0 )
    }
  }

}
