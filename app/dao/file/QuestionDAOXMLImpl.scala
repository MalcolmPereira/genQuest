package dao.file

import java.util.concurrent.atomic.AtomicLong

import akka.actor.Props
import dao.QuestionDAO
import model.{AnswerOption, AnswerOptions, Question}
import play.api.Play.current
import play.api.libs.concurrent.Akka

import scala.collection.mutable.ListBuffer
import scala.xml.{Elem, Node}

object QuestionDAOXMLImpl extends QuestionDAO {

  val fileManagerActor = Akka.system.actorOf(Props[FileManagerActor])

  private val idGenerator = new AtomicLong(
  {
    var idList       = new ListBuffer[Int]()
    scala.xml.XML.loadFile("conf/question.xml") match {
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
    scala.xml.XML.loadFile("conf/question.xml") match {
      case <questions>{questions @ _*}</questions> => {
        for (question <- questions) {
          if ((question \ "questionId").text.trim.length > 0 && (question \ "questionId").text.trim.toInt > 0) {
              questionList += getQuestion(question)
          }
        }
      }
    }
    questionList.toList
  }

  override def findQuestion(questionId: Integer): Question = {
    val questionNode = scala.xml.XML.loadFile("conf/question.xml")
    questionNode match {
      case <questions>{questions @ _*}</questions> => {
        for (question <- questions) {
          if ((question \ "questionId").text.trim.length > 0 && (question \ "questionId").text.trim.toInt > 0 && (question \ "questionId").text.trim.toInt == questionId) {
            return getQuestion(question)
          }
        }
      }
    }
    null
  }

  override def findQuestionsByCategoryID(categoryIDs: List[Integer]): List[Question] = {
    var questionList  = new ListBuffer[Question]()
    val questionNode = scala.xml.XML.loadFile("conf/question.xml")
    questionNode match {
      case <questions>{questions @ _*}</questions> => {
        for (question <- questions) {
          if ((question \ "questionId").text.trim.length > 0 && (question \ "questionId").text.trim.toInt > 0 && (question \ "categoryId").text.trim.length > 0  && categoryIDs.contains((question \ "categoryId").text.trim.toInt)) {
            questionList += getQuestion(question)
          }
        }
      }
    }
    questionList.toList
  }

  override def addQuestion(question: Question): Integer = {
      val questionID     = idGenerator.getAndIncrement.toInt
      fileManagerActor ! ManageNode(getQuestionNode(question, questionID),"QUESTION","ADD")
      questionID
  }

  override def updateQuestion(question: Question): Question = {
      scala.xml.XML.loadFile("conf/question.xml") match {
          case <questions>{questions @ _*}</questions> => {
              for (question_ <- questions) {
                  if ((question_ \ "questionId").text.trim.length > 0 && (question_ \ "questionId").text.trim.toInt > 0 && (question_ \ "questionId").text.trim.toInt == question.id) {
                    fileManagerActor ! ManageNode(getQuestionNode(question, question_),"QUESTION","UPDATE")
                    return question
                  }
              }
          }
      }
      question
  }

  override def deleteQuestion(questionId: Integer): Integer = {
    var nodeCounter  = 0
    scala.xml.XML.loadFile("conf/question.xml") match {
      case <questions>{questions @ _*}</questions> => {
        for (question_ <- questions) {
          if ((question_ \ "questionId").text.trim.length > 0 && (question_ \ "questionId").text.trim.toInt > 0 && (question_ \ "questionId").text.trim.toInt == questionId) {
                fileManagerActor ! ManageNode(question_,"QUESTION","DELETE")
                nodeCounter  = nodeCounter  + 1
                return nodeCounter
          }
        }
      }
    }
    nodeCounter
  }

  private def getQuestion(question: Node) :Question = {
    new Question((question \ "questionId").text.toInt,
    (question \ "categoryId").text.toInt,
    (question \ "questionText").text,
    (question \ "questionAnswer").text,
    {
      if((question \"answerOptions") != null && (question \"answerOptions").text != null && (question \"answerOptions").text.trim().length > 0){
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

  private def getQuestionNode(question: Question, questionID: Int) :Node = {
    scala.xml.XML.loadString(
      "<question><questionId>"+questionID +
        "</questionId><categoryId>"+question.category+
        "</categoryId><questionText>"+question.question+
        "</questionText><questionAnswer>"+question.answer+"</questionAnswer>"+
        {
          if(question.options != null){
            var optionString = "<answerOptions multipleCorrect=\"" + question.options.multipleCorrect.toString + "\">"
            for (answerOptions <- question.options.answerOptions) {
              optionString   += "<answerOption><name>"+answerOptions.optionName
              optionString   += "</name><correct>"+answerOptions.optionCorrect
              optionString   += "</correct></answerOption>"
            }
            optionString     += "</answerOptions></question>"
            optionString
          }else{
            "</question>"
          }
        }
    )
  }

  private def getQuestionNode(question: Question, questionNode: Node)  :Node = {
    scala.xml.XML.loadString(
          "<question><questionId>"+ (questionNode \ "questionId").text.toInt +
          "</questionId><categoryId>"+(questionNode \ "categoryId").text.toInt+
          "</categoryId><questionText>"+question.question+
          "</questionText><questionAnswer>"+question.answer+"</questionAnswer>"+
          {
            if(question.options != null){
              var optionString = "<answerOptions multipleCorrect=\"" + question.options.multipleCorrect.toString + "\">"
              for (answerOptions <- question.options.answerOptions) {
                optionString   += "<answerOption><name>"+answerOptions.optionName
                optionString   += "</name><correct>"+answerOptions.optionCorrect
                optionString   += "</correct></answerOption>"
              }
              optionString     += "</answerOptions></question>"
              optionString
            }else{
              "</question>"
            }
          }
      )
  }
}
