package model

class Question(val questionId: Integer,
               val categoryId: Integer,
               val questionText: String,
               val questionAnswer: String,
               val answerOptions: AnswerOptions) {

  def this(questionId: Integer,
           categoryId: Integer,
           questionText: String,
           questionAnswer: String) {
    this(questionId,categoryId,questionText,questionAnswer,null)
  }

  def this(categoryId: Integer,
           questionText: String,
           questionAnswer: String) {
    this(0,categoryId,questionText,questionAnswer,null)
  }

  def this(categoryId: Integer,
           questionText: String,
           questionAnswer: String,
           answerOptions: AnswerOptions) {
    this(0,categoryId,questionText,questionAnswer,answerOptions)
  }

  def id = questionId

  def category = categoryId

  def question = questionText

  def answer  = questionAnswer

  def options = answerOptions

}


