package model

class Question(questionId: Integer,
               questionName: String,
               questionDescription: String,
               questionText: String,
               questionAnswer: String,
               answerOptions: AnswerOptions) {

  def this(questionId: Integer,
           questionName: String,
           questionDescription: String,
           questionText: String,
           questionAnswer: String) {
    this(questionId,questionName,questionDescription,questionText,questionAnswer,null)
  }

  def id = questionId

  def name = questionName

  def description = questionDescription

  def question = questionText

  def answer  = questionAnswer

  def options = answerOptions
}


