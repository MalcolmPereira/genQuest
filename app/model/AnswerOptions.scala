package model

class AnswerOptions(answerOptionList: List[AnswerOption],multiple: Boolean) {
  def answerOptions    = answerOptionList

  def multipleCorrect  = multiple
}
