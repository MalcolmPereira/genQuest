package dao

import org.scalatest._
import model.{Question, AnswerOptions,AnswerOption}

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
          if(question.options != null ){
              assert(question.options.answerOptions != null)
              assert(question.options.answerOptions.size > 0)
          }
      }
  }

  "QuestionDAO " should " Return valid question by id " in {
    val question = QuestionDAOXMLImpl.findQuestion(1)
    assert(question != null)
    assert(question.id != null)
    assert(question.id > 0)
    assert(question.category != null)
    assert(question.category > 0)
    assert(question.question != null)
    assert(question.question.length > 0)
    assert(question.answer != null)
    assert(question.answer.length > 0)
    if(question.options != null ){
      assert(question.options.answerOptions != null)
      assert(question.options.answerOptions.size > 0)
    }
  }

  "QuestionDAO " should " Return null question by invalid id " in {
    assert(QuestionDAOXMLImpl.findQuestion(0) == null)
  }

  "QuestionDAO " should " Return all questions for category list" in {
    val questions = QuestionDAOXMLImpl.findQuestionsByCategoryID(List(1))
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
      if(question.options != null ){
        assert(question.options.answerOptions != null)
        assert(question.options.answerOptions.size > 0)
      }
    }
  }

  "QuestionDAO " should " Return null questions for invalid category list" in {
     assert(QuestionDAOXMLImpl.findQuestionsByCategoryID(List(0)).size == 0)
  }

  "QuestionDAO " should " Return add valid question " in {
    val answerOptionList = List(new AnswerOption("option1",false),new AnswerOption("option2",false),new AnswerOption("option3",false),new AnswerOption("option4",true))
    val answerOption     = new AnswerOptions(answerOptionList,false)
    val question         = new Question(1,"Some Question","Some Answer",answerOption)
    val questionId       = QuestionDAOXMLImpl.addQuestion(question)
    assert( questionId > 0)
  }
}
