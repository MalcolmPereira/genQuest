package dao.file

import akka.actor.{Actor, ActorLogging}

import scala.xml.Node

case class UserAddNode(user: Node)
case class UserNode(userList: List[Node])
case class CategoryAddNode(category: Node)
case class CategoryNode(categoryList: List[Node])
case class QuestionAddNode(question: Node)
case class QuestionNode(questionList: List[Node])

class FileManagerActor extends Actor with ActorLogging {

    override def receive: Receive = {
        case UserAddNode(user) => {
          log.info("Saving new User "+user)
          scala.xml.XML.save("conf/login.xml", user, "UTF-8", false, null)
        }
        case UserNode(userList) => {
          log.info("Updating User List "+userList)
          scala.xml.XML.save("conf/login.xml", <users>{for(user <- userList) yield user}</users>, "UTF-8", false, null)
        }
        case CategoryAddNode(category) => {
          log.info("Saving new Category "+category)
          scala.xml.XML.save("conf/category.xml", category, "UTF-8", false, null)
        }
        case CategoryNode(categoryList) => {
          log.info("Updating Category List "+categoryList)
          scala.xml.XML.save("conf/category.xml", <categories>{for(category <- categoryList) yield category}</categories>, "UTF-8", false, null)
        }
        case QuestionAddNode(question) => {
          log.info("Saving new Question "+question)
          scala.xml.XML.save("conf/question.xml", question, "UTF-8", false, null)
        }
        case QuestionNode(questionList) => {
          log.info("Updating Question List "+questionList)
          scala.xml.XML.save("conf/question.xml", <questions>{for(question <- questionList) yield question}</questions>, "UTF-8", false, null)
        }
    }
}
