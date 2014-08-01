package model

class Question(questionId: Integer,
               categoryId: Integer,
               questionText: String,
               questionAnswer: String,
               answerOptions: AnswerOptions) {

  def this(questionId: Integer,
           categoryId: Integer,
           questionText: String,
           questionAnswer: String) {
    this(questionId,categoryId,questionText,questionAnswer,null)
  }

  def id = questionId

  def category = categoryId

  def question = questionText

  def answer  = questionAnswer

  def options = answerOptions
}


