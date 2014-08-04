package controllers

import dao._
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

  //User DAO
  val categoryDAO:CategoryDAO = CategoryDAOXMLImpl

  //User DAO
  val questionDAO:QuestionDAO = QuestionDAOXMLImpl

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

  //Category Selection Form
  val categoryForm = Form (
    tuple(
          "categoryName"  -> nonEmptyText,
          "categoryDesc"  -> nonEmptyText
         )verifying ("Category already exists please choose another category name.", result => result match {
           case (categoryName,categoryDesc) => categoryDAO.findCategory(categoryName) == null
         }
    )
  )

  //Index Page
  def index = Action { implicit request =>
    Ok(views.html.index(categoryDAO.listCategories(),selectCategoryForm,getHeader))
  }

  //Generate Questions
  def genquest = Action {  implicit request =>
    selectCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        implicit val errorStr: String = formWithErrors.errors(0).message
        BadRequest(views.html.index(categoryDAO.listCategories(), formWithErrors,getHeader))
      }
      ,
      success => {
        Ok(views.html.genquest(questionDAO.listQuestions(), getHeader))
      }
    )
  }

  //Login Action
  def login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          val header    =  views.html.header(formWithErrors,null,null,null)
          BadRequest(views.html.index(categoryDAO.listCategories(),selectCategoryForm,header ))
      }
      ,
      user => {
          val userData  =  userDAO.findUser(user._1, user._2)
          val header    =  views.html.header(loginForm,userData.id.toString,userData.firstName,userData.lastName)
          Ok(views.html.index(categoryDAO.listCategories(),selectCategoryForm,header)).withSession(
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
    Ok(views.html.index(categoryDAO.listCategories(),selectCategoryForm,header)).withNewSession
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
        Ok(views.html.index(categoryDAO.listCategories(),selectCategoryForm,header)).withSession(
          "userID" -> userID.toString,
          "userFirstName" -> user._3,
          "userLastName" -> user._4
        )
      }
    )
  }

  def editcategory = Action {  implicit request =>
    if(request.session.get("userID").isDefined ){
      Ok(views.html.editcategory(categoryDAO.listCategories(),categoryForm,getHeader))
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),selectCategoryForm,getHeader))
    }
  }

  def addcategory = Action { implicit request =>
    if(request.session.get("userID").isDefined ){
      categoryForm.bindFromRequest.fold(
        formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          BadRequest(views.html.editcategory(categoryDAO.listCategories(), formWithErrors,getHeader))
        }
        ,
        success => {
          categoryDAO.addCategory(new Category(success._1,success._2))
          Ok(views.html.editcategory(categoryDAO.listCategories(),categoryForm,getHeader))
        }
      )



    }else{
      Ok(views.html.index(categoryDAO.listCategories(),selectCategoryForm,getHeader))
    }

  }

  def editquestion = Action {  implicit request =>
    if(request.session.get("userID").isDefined ){
      Ok(views.html.editquestion(getHeader))
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),selectCategoryForm,getHeader))
    }
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

}