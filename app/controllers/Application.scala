package controllers

import dao.UserDAO
import dao.UserDAOXMLImpl
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.twirl.api.Html
import model._
import scala.xml.Elem
import scala.xml.Node
import util.BCryptUtil

object Application extends Controller {

  //Implicit Error String
  implicit val errorStr: String = ""

  //User DAO
  val userDAO:UserDAO = UserDAOXMLImpl

  //Login Form for Header Login Control
  val loginForm = Form(
      tuple(
          "username" -> nonEmptyText,
	        "password" -> nonEmptyText
	    ) verifying ("Invalid User Name / Password, Please register if you do not have a account yet.", result => result match {
          case (username, password) => userDAO.findUser(username, password) != null
        }
      )
  )

  //Registration Form for User Registeration
  val userDataForm = Form(
      tuple(
           "username"  -> nonEmptyText,
  	       "password"  -> nonEmptyText,
		       "firstname" -> nonEmptyText,
		       "lastName"  -> nonEmptyText
  	  )verifying ("User Name already exists please choose another user name.", result => result match {
           case (username, password,firstname,lastName) => userDAO.findUser(username) == null
       }
      )
  )

  //Category Selection Form
  val selectCategoryForm = Form(
     single(
           "categoryCheckBox" -> list(number)
     )verifying ("Please select at least one category before submitting.", result => result match {
           case (categoryCheckBox) => categoryCheckBox != null && categoryCheckBox.length > 0
      }
     )
  )

  //Index Page
  def index = Action { implicit request =>
    Ok(views.html.index(categoryList,selectCategoryForm,getHeader))
  }

  //Generate Questions
  def genQuest = Action {  implicit request =>
    selectCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        implicit val errorStr: String = formWithErrors.errors(0).message
        BadRequest(views.html.index(categoryList, formWithErrors,getHeader))
      }
      ,
      success => {
        Ok(views.html.genQuest(questionList, getHeader))
      }
    )
  }

  //Login Action
  def login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          val header    =  views.html.header(formWithErrors,null,null,null)
          BadRequest(views.html.index(categoryList,selectCategoryForm,header ))
      }
      ,
      user => {
          val userData  =  userDAO.findUser(user._1, user._2)
          val header    =  views.html.header(loginForm,userData.id.toString,userData.firstName,userData.lastName)
          Ok(views.html.index(categoryList,selectCategoryForm,header)).withSession(
              "userID"        -> userData.id.toString,
              "userFirstName" -> userData.firstName,
              "userLastName"  -> userData.lastName
          )
      }
    )
  }

  //Logout Action
  def logout = Action {  implicit request =>
    val header = views.html.header(loginForm,null,null,null)
    Ok(views.html.index(categoryList,selectCategoryForm,header)).withNewSession
  }

  //Register User
  def register = Action {  implicit request =>
    val header = views.html.registerheader()
    Ok(views.html.register(userDataForm,header))
  }

  //Add New User
  def adduser = Action {  implicit request =>
    userDataForm.bindFromRequest.fold(
      formWithErrors => {
        implicit val errorStr: String = formWithErrors.errors(0).message
        val header = views.html.registerheader()
        BadRequest(views.html.register(formWithErrors,header)).withNewSession
      }
      ,
      user => {
        val userID     = userDAO.addUser(new User(0,user._1,user._2,user._3,user._4))
        val header     = views.html.header(loginForm,userID.toString,user._3,user._4)
        Ok(views.html.index(categoryList,selectCategoryForm,header)).withSession(
          "userID" -> userID.toString,
          "userFirstName" -> user._3,
          "userLastName" -> user._4
        )
      }
    )
  }

  //Get User ID from session
  def getUserID()(implicit request: RequestHeader) : Int = {
    if(request.session.get("userID").isDefined ){
      return request.session.get("userID").get.toInt
    }else{
      return 0
    }
  }

  //Get Header HTML
  def getHeader()(implicit request: RequestHeader) : Html = {
    var userID        = ""
    var userFirstName = ""
    var userLastName  = ""
    if(request.session.get("userID").isDefined ){
      userID = request.session.get("userID").get
    }
    if(request.session.get("userFirstName").isDefined ){
      userFirstName = request.session.get("userFirstName").get
    }
    if(request.session.get("userLastName").isDefined ){
      userLastName = request.session.get("userLastName").get
    }
    return views.html.header(loginForm,userID,userFirstName,userLastName)
  }

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
}