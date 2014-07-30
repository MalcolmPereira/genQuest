package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import scala.xml.XML
import model._
import scala.xml.Elem
import scala.xml.NodeSeq
import scala.xml.Node
import hashutil.BCryptUtil
import hashutil.IDGenerator
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
  //Select Category Form
  val selectedCategoryForm = Form(
     single(
          "categoryCheckBox" -> list(number) 
     )verifying ("Invalid username or password", result => result match {
            case (categoryCheckBox) => categoryCheckBox != null && categoryCheckBox.length > 0
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
	  var userID = ""
	  var userFirstName = ""
	  var userLastName = ""
	  if(request.session.get("userID").isDefined ){
	     userID = request.session.get("userID").get
	  }
	  if(request.session.get("userFirstName").isDefined ){
	     userFirstName = request.session.get("userFirstName").get
	  }
	  if(request.session.get("userLastName").isDefined ){
	     userLastName = request.session.get("userLastName").get
	  }
	  val header = views.html.header(loginForm,userID,userFirstName,userLastName)   
	  Ok(views.html.index(categoryList,selectedCategoryForm,userID,header,""))
   }
  
  //Generate Questions
  def genQuest = Action {  implicit request =>
	  var userID = ""
	  var userFirstName = ""
	  var userLastName = ""
	  if(request.session.get("userID").isDefined ){
	     userID = request.session.get("userID").get
	  }
	  if(request.session.get("userFirstName").isDefined ){
	     userFirstName = request.session.get("userFirstName").get
	  }
	  if(request.session.get("userLastName").isDefined ){
	     userLastName = request.session.get("userLastName").get
	  }
	  val header   = views.html.header(loginForm,userID,userFirstName,userLastName)
    val errorStr = "Please select at least one category before submitting."
	  
	  selectedCategoryForm.bindFromRequest.fold(
		formWithErrors => {
			BadRequest(views.html.index(categoryList, formWithErrors,userID,header,errorStr))
		}	
		,
		success => {
	 	   Ok(views.html.genQuest(questionList, header,""))
 		}	
	  )		 
  }
  
  //Login Action
  def login = Action { 
	  implicit request =>
	  	loginForm.bindFromRequest.fold(
   		formWithErrors => {
			val header = views.html.header(formWithErrors,"","","")
      val errorStr = "Invalid User Name / Password, Please register if you do not have a account yet."
			BadRequest(views.html.index(categoryList,selectedCategoryForm,"",header,errorStr))
		}	,
   	  	user => {
		  val userData  =  getUser(user._1, user._2)
		  val header = views.html.header(loginForm,userData.id.toString,userData.firstName,userData.lastName) 
		  Ok(views.html.index(categoryList,selectedCategoryForm,userData.id.toString,header,"")).withSession(
			  "userID" -> userData.id.toString,
			  "userFirstName" -> userData.firstName,
			  "userLastName" -> userData.lastName
		  )
      }
    )
  }
  
  //Logout Action
  def logout = Action {  implicit request =>
	val header = views.html.header(loginForm,null,null,null) 
	Ok(views.html.index(categoryList,selectedCategoryForm,null,header,"")).withNewSession
  }
  
  //Register User
  def register = Action {  implicit request => 
	   val header = views.html.registerheader() 
	   Ok(views.html.register(userDataForm,header,""))
  }	  
  
  //REgister User
  def adduser = Action {  implicit request => 
	   	userDataForm.bindFromRequest.fold(
    		formWithErrors => {
				val header = views.html.registerheader()
        val errorStr = "User Name already exists please choose another user name."
				BadRequest(views.html.register(formWithErrors,header,errorStr)).withNewSession
			}	,
    	  	user => {
				
			 val nodeString   = "<user><userID>"+IDGenerator.next+
			                    "</userID><userName>"+user._1+
								"</userName><userPassword>"+BCryptUtil.create(user._2)+
								"</userPassword><userFirstName>"+user._3+
								"</userFirstName><userLastName>"+user._4+
								"</userLastName></user>"
			 val nodeXML =xml.XML.loadString(nodeString)				
			 val loginNode = xml.XML.loadFile("conf/login.xml")
			 println(loginNode)
			 val loginNodeUpdated = addChild(loginNode, nodeXML)
			 println(loginNodeUpdated)
			 xml.XML.save("conf/login.xml", loginNodeUpdated, "UTF-8", false, null)
				
 		     val userData  =  getUser(user._1, user._2)
   		  	 val header = views.html.header(loginForm,userData.id.toString,userData.firstName,userData.lastName) 
   		     Ok(views.html.index(categoryList,selectedCategoryForm,userData.id.toString,header,"")).withSession(
   			  "userID" -> userData.id.toString,
   			  "userFirstName" -> userData.firstName,
   			  "userLastName" -> userData.lastName
   		     )
			}
	   )
  }	 
  
  def addChild(n: Node, newChild: Node) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) =>
      Elem(prefix, label, attribs, scope, child ++ newChild : _*)
    case _ => error("Can only add children to elements!")
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