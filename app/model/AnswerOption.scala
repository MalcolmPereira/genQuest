package model

case class AnswerOption(val name: String, val correct: Boolean) {

  def optionName    = name

  def optionCorrect = correct
}
