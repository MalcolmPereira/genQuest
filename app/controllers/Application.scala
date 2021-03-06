package controllers

import dao._
import dao.file.{UserDAOXMLImpl, QuestionDAOXMLImpl, CategoryDAOXMLImpl}
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.twirl.api.Html
import model._
import scala.collection.mutable.ListBuffer

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
      )
      verifying ("Invalid Password, Password should not contain > < and \" characters.", result => result match {
        case (username, password,firstname,lastName) => {
            if(password.contains(">") || password.contains(">") || password.contains("\"") ){
              false
            }else {
              true
            }
          }
        }
      )
      verifying ("User Name already exists please choose another user name.", result => result match {
           case (username, password,firstname,lastName) => userDAO.findUser(username) == null
        }
      )
  )

  //Category Selection Form
  val selectCategoryForm = Form(
     single(
           "categoryId" -> list(number)
     )verifying ("Please select at least one category before submitting.", result => result match {
           case (categoryId) => categoryId != null && categoryId.length > 0
      }
     )
  )

  //Category Selection Form
  val matchCategoryForm = Form(
    single(
      "categoryId" -> number
    )
  )

  //Category Selection Form
  val categoryForm = Form (
    tuple(
          "categoryName"  -> nonEmptyText,
          "categoryDesc"  -> nonEmptyText,
          "categoryId"    -> optional(number)
         )verifying ("Category already exists please choose another category name.", result => result match {
           case (categoryName,categoryDesc,categoryId) => {
             if(categoryId.isEmpty) {
               categoryDAO.findCategory(categoryName) == null
             }else{
               true
             }
           }
         }
    )
  )

  //Category Selection Form
  val selectQuestionForm = Form(
    single(
      "questionId" -> number
    )
  )

  //Question Form
  val addQuestionForm = Form(
    tuple(
      "categoryId"       -> number,
      "question"         -> nonEmptyText,
      "answer"           -> nonEmptyText,
      "questionId"       -> optional(number),
      "answerOptions"    -> optional(boolean),
      "optionName"       -> optional(list(text)),
      "optionCorrect"    -> optional(list(boolean))
    )
  )

  //Submitted Question Id Form
  val submitQuestionsForm = Form(
    single(
      "questionId" -> list(number)
    )
  )

  //Index Page
  def index = Action { implicit request =>
    Ok(views.html.index(categoryDAO.listCategories(),getHeader))
  }

  //Generate Questions
  def genquest = Action {  implicit request =>
    selectCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        implicit val errorStr: String = formWithErrors.errors(0).message
        BadRequest(views.html.index(categoryDAO.listCategories(), getHeader))
      }
      ,
      success => {
        var catIDList  = new ListBuffer[Integer]()
        for(catid <- success){
          catIDList += catid.toInt
        }
        Ok(views.html.genquest(questionDAO.findQuestionsByCategoryID(catIDList.toList),null, getHeader))
      }
    )
  }

  def submitanswers = Action { implicit request =>
    val requestVal      = request.body.asFormUrlEncoded
    var questionList    = new ListBuffer[Question]()
    var submittedMap    = Map.newBuilder[Integer, SubmittedAnswers]
    //println(requestVal)
    for (questionId <- submitQuestionsForm.bindFromRequest().get){
      val question   = questionDAO.findQuestion(questionId)
      questionList   += question
      var answerDesc = "";
      if(requestVal.get.contains("answerOptionDesc["+questionId+"]")){
        answerDesc = requestVal.get("answerOptionDesc["+questionId+"]")(0)
      }
      if(requestVal.get.contains("answerOptionLength["+questionId+"]")){
        var answerList     = new ListBuffer[String]
        val answerOptionLength = requestVal.get("answerOptionLength["+questionId+"]")(0).toInt
        for (i <- 0 until answerOptionLength) {
          if(requestVal.get.contains("answerOption["+questionId+" "+i+"]")){
            answerList += requestVal.get("answerOption["+questionId+" "+i+"]")(0)
          }
        }
        if(requestVal.get.contains("answerOption["+questionId+"]")){
            answerList += requestVal.get("answerOption["+questionId+"]")(0)
        }
        submittedMap += ((question.id,new SubmittedAnswers(answerDesc,answerList.toList)))
      }else{
        submittedMap += ((question.id,new SubmittedAnswers(answerDesc,null)))
      }
    }
    Ok(views.html.genquest(questionList.toList,submittedMap.result(), getHeader))
  }


  //Login Action
  def login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          val header    =  views.html.header(formWithErrors,null,null,null)
          BadRequest(views.html.index(categoryDAO.listCategories(),header ))
      }
      ,
      user => {
          val userData  =  userDAO.findUser(user._1, user._2)
          val header    =  views.html.header(loginForm,userData.id.toString,userData.firstName,userData.lastName)
          Ok(views.html.index(categoryDAO.listCategories(),header)).withSession(
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
    Ok(views.html.index(categoryDAO.listCategories(),header)).withNewSession
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
        val userID     = userDAO.addUser(new User(0,
            user._1.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"),
            user._2.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"),
            user._3.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"),
            user._4.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"))
        )
        val header     = views.html.header(loginForm,userID.toString,user._3,user._4)
        Ok(views.html.index(categoryDAO.listCategories(),header)).withSession(
          "userID" -> userID.toString,
          "userFirstName" -> user._3,
          "userLastName" -> user._4
        )
      }
    )
  }

  //Edit Category
  def editcategory = Action {  implicit request =>
    if(request.session.get("userID").isDefined ){
      Ok(views.html.editcategory(categoryDAO.listCategories(),getHeader))
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),getHeader))
    }
  }

  //Add Category
  def addcategory = Action { implicit request =>
    if(request.session.get("userID").isDefined ){
      categoryForm.bindFromRequest.fold(
        formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          BadRequest(views.html.editcategory(categoryDAO.listCategories(), getHeader))
        }
        ,
        success => {
          categoryDAO.addCategory(new Category(success._1.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"),
                                               success._2.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"))
                                 )
          Ok(views.html.editcategory(categoryDAO.listCategories(),getHeader))
        }
      )
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),getHeader))
    }
  }

  //Update Category
  def updatecategory = Action { implicit request =>
    if(request.session.get("userID").isDefined ){
      categoryForm.bindFromRequest.fold(
        formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          BadRequest(views.html.editcategory(categoryDAO.listCategories(), getHeader))
        }
        ,
        success => {
          categoryDAO.updateCategory(new Category(success._3.get.toInt,
                                          success._1.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"),
                                          success._2.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"))
                                    )
          Ok(views.html.editcategory(categoryDAO.listCategories(),getHeader))
        }
      )
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),getHeader))
    }
  }

  //Delete Category
  def deletecategory = Action { implicit request =>
    if(request.session.get("userID").isDefined ){
      selectCategoryForm.bindFromRequest.fold(
        formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          BadRequest(views.html.editcategory(categoryDAO.listCategories(),getHeader))
        }
        ,
        success => {
          if(questionDAO.findQuestionsByCategoryID(List(success(0).toInt)).nonEmpty){
            implicit val errorStr: String = "Category cannot be deleted since there are questions associated to it."
            Ok(views.html.editcategory(categoryDAO.listCategories(),getHeader))

          }else{
            val rowDeleted = categoryDAO.deleteCategory(success(0).toInt)
            if(rowDeleted ==  0 ){
              implicit val errorStr: String = "Category delete unsuccessful"
            }
            Ok(views.html.editcategory(categoryDAO.listCategories(),getHeader))
          }
        }
      )
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),getHeader))
    }
  }

  def editquestion = Action {  implicit request =>
    if(request.session.get("userID").isDefined ){
      val selectedCategory = matchCategoryForm.bindFromRequest.value
      if(selectedCategory.nonEmpty && selectedCategory.get > 0){
        Ok(views.html.editquestion(selectedCategory.get,categoryDAO.listCategories(),questionDAO.findQuestionsByCategoryID(List(selectedCategory.get)), getHeader))
      }else{
        Ok(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
      }
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),getHeader))
    }
  }

  def addquestion = Action { implicit request =>
    if(request.session.get("userID").isDefined ){
      addQuestionForm.bindFromRequest.fold(
        formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          BadRequest(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
        }
        ,
        success => {
          val catId       = success._1
          val question    = success._2.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;")
          val answer      = success._3.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;")
          val questionId  = success._4
          if(success._5.nonEmpty && success._6.nonEmpty && success._7.nonEmpty){
            var optionList = new ListBuffer[AnswerOption]()
            for( (optionName,i) <- success._6.get.zipWithIndex){
              optionList += new AnswerOption(optionName.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"),success._7.get(i))
            }
            val answerOption = new AnswerOptions(optionList.toList,success._5.get)
            questionDAO.addQuestion(new Question(catId,question,answer,answerOption))
            Ok(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
          }else{
            questionDAO.addQuestion(new Question(catId,question,answer))
            Ok(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
          }
        }
      )
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),getHeader))
    }
  }

  def updatequestion = Action { implicit request =>
    if(request.session.get("userID").isDefined ){
      addQuestionForm.bindFromRequest.fold(
        formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          BadRequest(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
        }
        ,
        success => {
          val catId       = success._1
          val question    = success._2.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;")
          val answer      = success._3.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;")
          val questionId  = success._4.get.toInt
          if(success._5.nonEmpty && success._6.nonEmpty && success._7.nonEmpty){
            var optionList = new ListBuffer[AnswerOption]()
            for( (optionName,i) <- success._6.get.zipWithIndex){
              optionList += new AnswerOption(optionName.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&#39;"),success._7.get(i))
            }
            val answerOption = new AnswerOptions(optionList.toList,success._5.get)
            questionDAO.updateQuestion(new Question(questionId,catId,question,answer,answerOption))
            Ok(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
          }else{
            questionDAO.updateQuestion(new Question(questionId,catId,question,answer))
            Ok(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
          }
        }
      )
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),getHeader))
    }
  }

  def deletequestion = Action { implicit request =>
    if(request.session.get("userID").isDefined ){
      selectQuestionForm.bindFromRequest.fold(
        formWithErrors => {
          implicit val errorStr: String = formWithErrors.errors(0).message
          BadRequest(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
        }
        ,
        success => {
          val rowDeleted = questionDAO.deleteQuestion(success)
          if(rowDeleted ==  0 ){
            implicit val errorStr: String = "Question delete unsuccessful"
          }
          Ok(views.html.editquestion(0,categoryDAO.listCategories(),questionDAO.listQuestions(), getHeader))
        }
      )
    }else{
      Ok(views.html.index(categoryDAO.listCategories(),getHeader))
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