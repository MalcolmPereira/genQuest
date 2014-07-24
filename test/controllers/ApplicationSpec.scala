package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

import hashutil.BCryptUtil

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the login page" in new WithApplication{
      val login = route(FakeRequest(GET, "/")).get
      status(login) must equalTo(OK)
      contentType(login) must beSome.which(_ == "text/html")
      contentAsString(login) must contain("Login")
    }
    
    "perform the login with valid user " in new WithApplication{
      val home = route(FakeRequest(POST, "/login")
          .withFormUrlEncodedBody(("username", "malcolm"),("password", "malcolm"))
      ).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("home screen!")
    }
    
    "perform the login with invalid user " in new WithApplication{
      val home = route(FakeRequest(POST, "/login")
          .withFormUrlEncodedBody(("username", "invalid"),("password", "invalid"))
      ).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Login")
    }
  }
  
  "BCryptUtil" should {
    "Should encrypt String " in {
        val encypted = BCryptUtil.create("malcolm")
        println(encypted)
    	println(BCryptUtil.check("malcolm","$2a$10$NeNjyd5AFIIsEB9VdWTp0Op339ewJFdh55EZ/0xFtupdm0r6ahO0q"))
        encypted must not be null
    	
    }
  }
}
