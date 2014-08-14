package dao.file

import model.{AnswerOption, AnswerOptions, Question}
import org.scalatest._
import org.specs2.mutable.Specification
import play.api.test.WithApplication

class QuestionDAOXMLImplSpec extends Specification {

  "QuestionDAO " should  {
      "Return all questions " in new WithApplication {
        val questions = QuestionDAOXMLImpl.listQuestions()
        assert(questions != null)
        assert(questions.size > 0)
        for (question <- questions) {
          assert(question.id != null)
          assert(question.id > 0)
          assert(question.category != null)
          assert(question.category > 0)
          assert(question.question != null)
          assert(question.question.length > 0)
          assert(question.answer != null)
          assert(question.answer.length > 0)
          if (question.options != null) {
            assert(question.options.answerOptions != null)
            assert(question.options.answerOptions.size > 0)
          }
        }
      }
  }

  "QuestionDAO " should {
      " Return valid question by id " in new WithApplication {
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
  }

  "QuestionDAO " should {
     " Return null question by invalid id " in new WithApplication {
      assert(QuestionDAOXMLImpl.findQuestion(0) == null)
    }
  }

  "QuestionDAO " should {
    " Return all questions for category list" in new WithApplication {
      val questions = QuestionDAOXMLImpl.findQuestionsByCategoryID(List(1))
      assert(questions != null)
      assert(questions.size > 0)
      for (question <- questions) {
        assert(question.id != null)
        assert(question.id > 0)
        assert(question.category != null)
        assert(question.category > 0)
        assert(question.question != null)
        assert(question.question.length > 0)
        assert(question.answer != null)
        assert(question.answer.length > 0)
        if (question.options != null) {
          assert(question.options.answerOptions != null)
          assert(question.options.answerOptions.size > 0)
        }
      }
    }
  }

  "QuestionDAO " should {
     " Return null questions for invalid category list" in new WithApplication {
      assert(QuestionDAOXMLImpl.findQuestionsByCategoryID(List(0)).size == 0)
    }
  }

  "QuestionDAO " should {
     " add/update and delete new question " in new WithApplication {
      val answerOptionList = List(new AnswerOption("option1",false),new AnswerOption("option2",false),new AnswerOption("option3",false),new AnswerOption("option4",true))
      val answerOption     = new AnswerOptions(answerOptionList,false)
      val question         = new Question(1,"Some Question","Some Answer",answerOption)
      val questionId       = QuestionDAOXMLImpl.addQuestion(question)
      assert( questionId > 0)
      val updatedAnswerOptionList = List(new AnswerOption("option1_Update",false),new AnswerOption("option2_Update",false),new AnswerOption("option3_Update",false),new AnswerOption("option4_Update",true))
      val updatedanswerOption     = new AnswerOptions(updatedAnswerOptionList,false)
      val updatequestion          = new Question(questionId,1,"New Updates Some Question","New Updates Some Answer",updatedanswerOption)
      val updatedQuestion         = QuestionDAOXMLImpl.updateQuestion(updatequestion)
      assert( updatedQuestion != null)
      val rowid = QuestionDAOXMLImpl.deleteQuestion(questionId)
      assert( rowid != 0 )
    }
  }
}
