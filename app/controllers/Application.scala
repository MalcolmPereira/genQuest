package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import scala.xml.XML
import model.User
import scala.xml.Elem
import hashutil.BCryptUtil
import play.api.data.validation.ValidationError

object Application extends Controller {
  //Login Form
  val loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
	  "password" -> nonEmptyText
	) verifying ("Invalid username or password", result => result match {
          case (username, password) => getUser(username, password) != null
    })
  ) 
  
  //Login Index Page
  def index = Action {
    Ok(views.html.index(loginForm))
  }
  
  //Login Action
  def login = Action { implicit request =>
   loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.index(formWithErrors)),
      user => {
        val userData  =  getUser(user._1, user._2)
        val userToken =  java.util.UUID.randomUUID().toString() + userData.id.toString +userData.name
        Ok(views.html.home(userData)).withSession("userSession" -> userToken.toString)
      }
    )
  }
  
  def genQuest = Action { implicit request =>
    request.session.get("userSession").map { userToken =>
    	Ok(views.html.genQuest(userToken))
    }.getOrElse {
    	Unauthorized("Oops, you are not connected")
    }
  }
  
  def editQuest = Action { implicit request =>
    request.session.get("userSession").map { userToken =>
    	Ok(views.html.editQuest(userToken))
    }.getOrElse {
    	Unauthorized("Oops, you are not connected")
    }
  }
  
  //Logout Action
  def logout = Action { implicit request =>
    Ok(views.html.index(loginForm)).withNewSession.flashing("success" -> "You are now logged out.")
  }
  
  //Get User
  def getUser(userName: String, userPassword: String) : User = {
     val loginNode = xml.XML.loadFile("conf/login.xml")
	 loginNode match {
	    case <users>{users @ _*}</users> => {
	    	for (user <- users) {
	    	  if((user \"userName").text == userName && BCryptUtil.check(userPassword,(user \"userPassword").text)){
	    		   return new User(
	    			  (user \"userID").text.toInt,
	    			  (user \"userName").text,
	    			  (user \"userFirstName").text,
	    			  (user \"userLastName").text
	    		  )
	    	  }
	    	}
	    	null
	    }
	 }
  }
}