package dao

import java.util.concurrent.atomic.AtomicLong

import model.Question

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
                                         (question \ "questionAnswer").text
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
