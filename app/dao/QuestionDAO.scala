package dao
import model.Question


trait QuestionDAO {
  def listQuestions(): List[Question]
  def listQuestions(questionIDs: List[Integer]): List[Question]
  def findQuestion(questionId: Integer): Question
  def listQuestionsByCategoryID(categoryIDs: List[Integer]): List[Question]
  def findQuestionsByCategoryID(categoryID: Integer): List[Question]
  def addQuestion(question: Question): Integer
  def updateQuestion(question: Question): Question
  def deleteQuestion(question: Question): Integer
}
