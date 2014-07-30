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
    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "renders the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must find("Generate Questions")
      contentAsString(home) must find("Select categories below and click on the submit button to generate questions.")
      contentAsString(home) must find("Sign in")

    }

    "perform Login with valid user " in new WithApplication {
        val home = route(FakeRequest(POST, "/login")
          .withFormUrlEncodedBody(("username", "malcolm"),("password", "malcolm"))
        ).get
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must find("Generate Questions")
        contentAsString(home) must find("Select categories below and click on the submit button to generate questions.")
        contentAsString(home) must find("Welcome")
        contentAsString(home) must find("Logout")
    }

    "perform Login with invalid user " in new WithApplication {
      val home = route(FakeRequest(POST, "/login")
        .withFormUrlEncodedBody(("username", "invalid"),("password", "invalid"))
      ).get
      status(home) must equalTo(400)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must find("Generate Questions")
      contentAsString(home) must find("Select categories below and click on the submit button to generate questions.")
      contentAsString(home) must find("Sign in")
      contentAsString(home) must find("Invalid User Name / Password, Please register if you do not have a account yet.")
    }
  }
}
