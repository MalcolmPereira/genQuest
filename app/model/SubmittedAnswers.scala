package model

case class SubmittedAnswers(val answerStr: String, val answerOptionList: List[String]) {
  def answer       = answerStr

  def answerOption = answerOptionList
}
