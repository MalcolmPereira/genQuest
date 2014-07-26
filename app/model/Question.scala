package model

class Question(questionId: Integer,
           questionName: String,
           questionDescription: String,
		   quest: String,
		   ans: String){
  def id           = questionId 
  def name  	   = questionName
  def description  = questionDescription
  def question     = quest
  def answer       = ans
}