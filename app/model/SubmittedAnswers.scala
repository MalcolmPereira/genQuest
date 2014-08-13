package model

case class SubmittedAnswers(val questionId: Integer, val answerStr: String, val answerOptionList: List[AnswerOption]) {
  def question     = questionId

  def answer       = answerStr

  def answerOption = answerOptionList
}
