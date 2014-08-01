package dao

import org.scalatest._
import model.Question

class QuestionDAOXMLImplSpec extends FlatSpec {

  "QuestionDAO " should " Return all questions " in {
    val questions = QuestionDAOXMLImpl.listQuestions()
    assert(questions != null)
    assert(questions.size > 0)
    for(question <- questions){
      assert(question.id != null)
      assert(question.id > 0)
      assert(question.category != null)
      assert(question.category > 0)
      assert(question.question != null)
      assert(question.question.length > 0)
      assert(question.answer != null)
      assert(question.answer.length > 0)
    }
   }

}
