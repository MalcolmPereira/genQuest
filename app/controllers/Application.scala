package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import scala.xml.XML
import model._
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
  //User Data Form
  val userDataForm = Form(
      tuple(
        "username"  -> nonEmptyText,
  	    "password"  -> nonEmptyText,
		"firstname" -> nonEmptyText,
		"lastName"  -> nonEmptyText
  	  )verifying ("Invalid user details", result => result match {
          case (username, password,firstname,lastName) => checkUserName(username) == null
    })
  )
  
  //Get Category TODO
  def categoryList() : List[Category]  = {
	  List(
	  	      new Category(1, "Java","Java Cate"),
	  		  new Category(2, "Spring","Spring Cate"),
	  		  new Category(3, "Web Service","WS Cate"),
	  		  new Category(4, "Test 4","WS Cate"),
	  		  new Category(5, "Test 5","WS Cate"),
	  		  new Category(6, "Test 6","WS Cate"),
	  		  new Category(7, "Test 7","WS Cate"),
	  		  new Category(8, "Test 8","WS Cate"),
	  		  new Category(9, "Test 9","WS Cate"),
	  		  new Category(10, "Test 10","WS Cate"),
	  		  new Category(11, "Test 11","WS Cate"),
	  		  new Category(12, "Test 12","WS Cate"),
	  		  new Category(13, "Test 13","WS Cate"),
	  		  new Category(14, "Test 14","WS Cate"),
	  		  new Category(15, "Test 15","WS Cate"),
	  		  new Category(16, "Test 16","WS Cate"),
	  		  new Category(17, "Test 17","WS Cate"),
	  		  new Category(18, "Test 18","WS Cate")
	  	)
  }
  
  //Get Questions TODO
  def questionList() : List[Question] = {
	  List(
	  		  new Question(1, "Java","Java Cate","question","answer"),
	  		  new Question(2, "Java","Java Cate","question","answer"),
	  		  new Question(3, "Java","Java Cate","question","answer"),
	  		  new Question(4, "Java","Java Cate","question","answer"),
	  		  new Question(5, "Java","Java Cate","question","answer"),
	  		  new Question(6, "Java","Java Cate","question","answer"),
	  		  new Question(7, "Java","Java Cate","question","answer"),
	  		  new Question(8, "Java","Java Cate","question","answer"),
	  		  new Question(9, "Java","Java Cate","question","answer"),
	  		  new Question(10, "Java","Java Cate","question","answer"),
	  		  new Question(11, "Java","Java Cate","question","answer")
	  	  )
  }
  
  //Index Page
  def index = Action { implicit request =>
	 if(request.session.get("userSession") != null ){
		 val userID = request.session.get("userSession").toString.substring(request.session.get("userSession").toString.lastIndexOf("GQ2014QG_")+9).replace(")","")
	     println("INDEX userID now"+userID.toInt)
		 Ok(views.html.index(categoryList,getUser(userID.toInt),loginForm))
		 }else{
	      Ok(views.html.index(categoryList,null,loginForm))
         }
  }
  
  //Generate Questions
  def genQuest = Action {  implicit request =>
	 if(request.session.get("userSession") != null ){
		 val userID = request.session.get("userSession").toString.substring(request.session.get("userSession").toString.lastIndexOf("GQ2014QG_")+9).replace(")","")
	     println("GEN QUEST userID now"+userID.toInt+" Get USer "+getUser(userID.toInt))
		 Ok(views.html.genQuest(questionList,getUser(userID.toInt),loginForm))
		 }else{
	  Ok(views.html.genQuest(questionList,null,loginForm))
      }
  }
  
  //Login Action
  def login = Action { 
	  implicit request =>
	  	loginForm.bindFromRequest.fold(
   		formWithErrors => BadRequest(views.html.index(categoryList(),null,formWithErrors)),
   	  	user => {
		  val userData  =  getUser(user._1, user._2)
		  val userToken =  java.util.UUID.randomUUID().toString()+"GQ2014QG_"+userData.id.toString 	
   		  Ok(views.html.index(categoryList,userData,loginForm)).withSession("userSession" -> userToken.toString)
      }
    )
  }
  
  //Logout Action
  def logout = Action {  implicit request =>
	Ok(views.html.index(List(),null,loginForm)).withNewSession.flashing("success" -> "You are now logged out.")
  }
  
  //REgister User
  def register = Action {  implicit request => 
	   Ok(views.html.register(null,userDataForm))
  }	  
  
  //REgister User
  def adduser = Action {  implicit request => 
	   	userDataForm.bindFromRequest.fold(
    		formWithErrors => BadRequest(views.html.register(null,formWithErrors)),
    	  	user => {
 		     val userData  =  getUser("malcolm", "malcolm")
 		     val userToken =  java.util.UUID.randomUUID().toString()+"GQ2014QG_"+userData.id.toString 	
    		 Ok(views.html.index(categoryList,userData,loginForm)).withSession("userSession" -> userToken.toString)
       }
	   )
  }	 
  
  //Chec User Name
  def checkUserName(userName: String) : User = {
     val loginNode = xml.XML.loadFile("conf/login.xml")
	 loginNode match {
	    case <users>{users @ _*}</users> => {
	    	for (user <- users) {
	    	  if((user \"userName").text == userName){
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
  
  //Get User
  def getUser(userId: Integer) : User = {
     val loginNode = xml.XML.loadFile("conf/login.xml")
	 loginNode match {
	    case <users>{users @ _*}</users> => {
	    	for (user <- users) {
				println("GOT userId "+userId)
				println("GOT (user userID).text "+(user \"userID").text)
	    	  if((user \"userID").text.trim.length > 0 &&  (user \"userID").text.toInt == userId){
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