package dao
import model.Question


trait QuestionDAO {
  def listQuestions(): List[Question]
  def findQuestion(questionId: Integer): Question
  def findQuestionsByCategoryID(categoryIDs: List[Integer]): List[Question]
  def addQuestion(question: Question): Integer
  def updateQuestion(question: Question): Question
  def deleteQuestion(questionId: Integer): Integer
}
