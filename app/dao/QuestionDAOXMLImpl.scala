package dao

import java.util.concurrent.atomic.AtomicLong

import model.{Question,AnswerOptions, AnswerOption}

import scala.collection.mutable.ListBuffer

object QuestionDAOXMLImpl extends QuestionDAO {

  private val idGenerator = new AtomicLong(
  {
    var idList       = new ListBuffer[Int]()
    val questionNode = scala.xml.XML.loadFile("conf/question.xml")
    questionNode match {
      case <questions>{questions @ _*}</questions> => {
        for (question <- questions) {
          if((question \"questionId").text.trim.length > 0 &&  (question \"questionId").text.trim.toInt > 0 ){
            idList += (question \"questionId").text.toInt
          }
        }
      }
    }
    idList.max + 1
  })

  override def listQuestions(): List[Question] = {
    var questionList  = new ListBuffer[Question]()
    val questionNode = scala.xml.XML.loadFile("conf/question.xml")
    questionNode match {
      case <questions>{questions @ _*}</questions> => {
        for (question <- questions) {
          if ((question \ "questionId").text.trim.length > 0 && (question \ "questionId").text.trim.toInt > 0) {
              questionList += new Question((question \ "questionId").text.toInt,
                (question \ "categoryId").text.toInt,
                (question \ "questionText").text,
                (question \ "questionAnswer").text,
                {
                  if((question \"answerOptions") != null){
                    var answerOptionsList = new ListBuffer[AnswerOption]()
                    for (answerOption <- (question \"answerOptions" \ "answerOption")) {
                      if((answerOption \"name").text.trim.length > 0 && (answerOption \"correct").text.trim.length > 0 ){
                        answerOptionsList += new AnswerOption(
                                                              (answerOption \"name").text.trim,
                                                              (answerOption \"correct").text.trim.toBoolean
                                              )
                      }
                    }
                    new AnswerOptions(answerOptionsList.toList,{(question \"answerOptions"\@"multipleCorrect").toBoolean})

                  }else{
                    null
                  }
                }
              )
          }
        }
      }
    }
    questionList.toList
  }

  override def listQuestions(questionIDs: List[Integer]): List[Question] = {
    null
  }

  override def findQuestion(questionId: Integer): Question = {
    null
  }

  override def listQuestionsByCategoryID(categoryIDs: List[Integer]): List[Question] = {
    null
  }


  override def findQuestionsByCategoryID(categoryID: Integer): List[Question] = {
    null
  }


  override def addQuestion(question: Question): Integer = {
    null
  }

  override def updateQuestion(question: Question): Question = {
    null
  }

  override def deleteQuestion(question: Question): Integer = {
    0
  }
}
