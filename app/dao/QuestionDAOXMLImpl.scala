package dao

import java.util.concurrent.atomic.AtomicLong

import model.{Question,AnswerOptions, AnswerOption}

import scala.collection.mutable.ListBuffer
import scala.xml.{Node, Elem}

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

  override def findQuestion(questionId: Integer): Question = {
    val questionNode = scala.xml.XML.loadFile("conf/question.xml")
    questionNode match {
      case <questions>{questions @ _*}</questions> => {
        for (question <- questions) {
          if ((question \ "questionId").text.trim.length > 0 && (question \ "questionId").text.trim.toInt > 0 && (question \ "questionId").text.trim.toInt == questionId) {
            return new Question((question \ "questionId").text.toInt,
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
    null
  }

  override def findQuestionsByCategoryID(categoryIDs: List[Integer]): List[Question] = {
    var questionList  = new ListBuffer[Question]()
    val questionNode = scala.xml.XML.loadFile("conf/question.xml")
    questionNode match {
      case <questions>{questions @ _*}</questions> => {
        for (question <- questions) {
          if ((question \ "questionId").text.trim.length > 0 && (question \ "questionId").text.trim.toInt > 0 && (question \ "categoryId").text.trim.length > 0  && categoryIDs.contains((question \ "categoryId").text.trim.toInt)) {
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

  override def addQuestion(question: Question): Integer = {
    val questionID     = idGenerator.getAndIncrement.toInt
    val checkCategory  = CategoryDAOXMLImpl.findCategory(question.category)
    if(checkCategory != null){
      val nodeString     = "<question><questionId>"+questionID +
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
      val nodeXML           = scala.xml.XML.loadString(nodeString)
      val loginNode         = scala.xml.XML.loadFile("conf/question.xml")
      val loginNodeUpdated  = loginNode match {
        case Elem(prefix, label, attribs, scope, child @ _*) => {
          Elem(prefix, label, attribs, scope, true, child ++ nodeXML: _*)
        }
      }
      scala.xml.XML.save("conf/question.xml", loginNodeUpdated, "UTF-8", false, null)
      return questionID
    }
    0
  }

  override def updateQuestion(question: Question): Question = {
    val questionNode  = scala.xml.XML.loadFile("conf/question.xml")
    var questionList  = new ListBuffer[Node]()
    questionNode match {
      case <questions>{questions @ _*}</questions> => {
        for (question_ <- questions) {
          if ((question_ \ "questionId").text.trim.length > 0 && (question_ \ "questionId").text.trim.toInt > 0 && (question_ \ "questionId").text.trim.toInt == question.id) {
               questionList  += {
                 scala.xml.XML.loadString(
                   "<question><questionId>"+ (question_ \ "questionId").text.toInt +
                   "</questionId><categoryId>"+(question_ \ "categoryId").text.toInt+
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
          }else{
            questionList  += question_
          }
        }
      }
    }
    val loginNodeUpdated = <questions>{for(question <- questionList) yield question}</questions>
    scala.xml.XML.save("conf/question.xml", loginNodeUpdated, "UTF-8", false, null)
    question
  }

  override def deleteQuestion(question: Question): Integer = {
    val questionNode  = scala.xml.XML.loadFile("conf/question.xml")
    var questionList  = new ListBuffer[Node]()
    var nodeCounter  = 0
    questionNode match {
      case <questions>{questions @ _*}</questions> => {
        for (question_ <- questions) {
          if ((question_ \ "questionId").text.trim.length > 0 && (question_ \ "questionId").text.trim.toInt > 0 && (question_ \ "questionId").text.trim.toInt != question.id) {
            questionList  += question_
          }else{
            nodeCounter  = nodeCounter  + 1
          }
        }
      }
    }
    val loginNodeUpdated = <questions>{for(question <- questionList) yield question}</questions>
    scala.xml.XML.save("conf/question.xml", loginNodeUpdated, "UTF-8", false, null)
    nodeCounter
  }
}
