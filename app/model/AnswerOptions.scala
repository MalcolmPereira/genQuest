package model

case class AnswerOptions(val answerOptionList: List[AnswerOption], val multiple: Boolean) {
  def answerOptions    = answerOptionList

  def multipleCorrect  = multiple
}
