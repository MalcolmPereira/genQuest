package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import scala.xml.XML
import model.User
import scala.xml.Elem
import hashutil.BCryptUtil

object Application extends Controller {
   val loginForm = Form(
		   tuple(
				  "username" -> text,
				  "password" -> text
			)
  )
  
  def index = Action {
    Ok(views.html.index(loginForm))
  }
  
  def login = Action { implicit request =>
    val (userName,userPassword) = loginForm.bindFromRequest.get
    val loginNode = xml.XML.loadFile("conf/login.xml")
    val user = checkLogin(loginNode, userName, userPassword)
    if(user != null){
       println(user.firstName)
       println(user.lastName)
       Ok(views.html.home("Your new application is ready."))
    
    }else{
       Ok(views.html.index(loginForm))
    }
  }
  
  def checkLogin(loginNode: Elem, userName: String, userPassword: String) : User = {
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