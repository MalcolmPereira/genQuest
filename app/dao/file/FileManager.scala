package dao.file

import play.Logger

import scala.collection.mutable.ListBuffer
import scala.xml.Node

case class ManageNode(node: Node, nodeType:String, operation: String)

object FileManager {

    def receive(node: ManageNode) :Unit  = {
         node.nodeType match {
                case "USER" => {
                    this.synchronized{
                      manageUserData(node.node,node.operation)
                    }
                }
                case "CATEGORY" => {
                    this.synchronized {
                      manageCategoryData(node.node,node.operation)
                    }
                }
                case "QUESTION" => {
                    this.synchronized {
                      manageQuestionData(node.node,node.operation)
                    }
                }
        }
    }

    private def manageUserData(userNode:Node,operation: String): Unit ={
        Logger.info("User: "+userNode+ " Operation: "+operation)
        var userList = new ListBuffer[Node]()
        operation match {
             case ("ADD" | "UPDATE")  =>{
                  println("operation match "+operation)
                  var newEntry = true
                  scala.xml.XML.loadFile("conf/login.xml") match {
                       case <users>{users @ _*}</users> => {
                            for (user_ <- users) {
                                println("UserNode valus is "+(userNode \"userID").text.trim.toInt)
                                  if( (user_ \"userID").text.trim.length > 0 &&
                                      (user_ \"userID").text.trim.toInt  > 0 &&
                                      (userNode \"userID").text.trim.length > 0 &&
                                      (userNode \"userID").text.trim.toInt  > 0 &&
                                      (user_ \"userID").text.trim.toInt == (userNode \"userID").text.trim.toInt ){
                                          userList  += userNode
                                          newEntry = false;
                                  }else{
                                          userList  += user_
                                  }
                            }
                            if(newEntry){
                              userList  += userNode
                            }
                       }
                  }
             }
             case "DELETE" =>{
                  scala.xml.XML.loadFile("conf/login.xml") match {
                        case <users>{users @ _*}</users> => {
                            for (user_ <- users) {
                                  if( (user_ \"userID").text.trim.length > 0 &&
                                      (user_ \"userID").text.trim.toInt  > 0 &&
                                      (userNode \"userID").text.trim.length > 0 &&
                                      (userNode \"userID").text.trim.toInt  > 0 &&
                                      (user_ \"userID").text.trim.toInt != (userNode \"userID").text.trim.toInt ){
                                      userList  += user_
                                  }
                            }
                        }
                  }
             }
        }
        scala.xml.XML.save("conf/login.xml", <users>{for(user <- userList) yield user}</users>, "UTF-8", false, null)
    }

    private def manageCategoryData(categoryNode:Node,operation: String): Unit ={
        Logger.info("Category: "+categoryNode+ " Operation: "+operation)
        var categoryList = new ListBuffer[Node]()
        operation match {
              case ("ADD" | "UPDATE")  =>{
                  var newEntry = true
                  scala.xml.XML.loadFile("conf/category.xml") match {
                      case <categories>{categories @ _*}</categories> => {
                          for (category_ <- categories) {
                              if ( (category_    \ "categoryId").text.trim.length > 0 &&
                                   (category_    \ "categoryId").text.trim.toInt  > 0 &&
                                   (categoryNode \ "categoryId").text.trim.length > 0 &&
                                   (categoryNode \ "categoryId").text.trim.toInt  > 0 &&
                                   (category_    \ "categoryId").text.trim.toInt == (categoryNode \ "categoryId").text.trim.toInt) {
                                 categoryList  += categoryNode
                                 newEntry = false;
                              }else{
                                 categoryList  += category_
                              }
                          }
                          if(newEntry){
                              categoryList  += categoryNode
                          }
                      }
                  }
              }
              case "DELETE" =>{
                  scala.xml.XML.loadFile("conf/category.xml") match {
                       case <categories>{categories @ _*}</categories> => {
                           for (category_ <- categories) {
                                if ((category_    \ "categoryId").text.trim.length > 0 &&
                                    (category_    \ "categoryId").text.trim.toInt  > 0 &&
                                    (categoryNode \ "categoryId").text.trim.length > 0 &&
                                    (categoryNode \ "categoryId").text.trim.toInt  > 0 &&
                                    (category_    \ "categoryId").text.trim.toInt != (categoryNode \ "categoryId").text.trim.toInt) {
                                    categoryList  += category_
                                }
                           }
                       }
                  }
              }
        }
        scala.xml.XML.save("conf/category.xml", <categories>{for(category <- categoryList) yield category}</categories>, "UTF-8", false, null)

    }

    private def manageQuestionData(questionNode:Node,operation: String): Unit ={
        Logger.info("QUESTION: "+questionNode+ " Operation: "+operation)
        var questionList  = new ListBuffer[Node]()
        operation match {
              case ("ADD" | "UPDATE")  =>{
                  var newEntry = true
                  scala.xml.XML.loadFile("conf/question.xml") match {
                       case <questions>{questions @ _*}</questions> => {
                            for (question_ <- questions) {
                                  if ( (question_ \ "questionId").text.trim.length > 0 &&
                                       (question_ \ "questionId").text.trim.toInt > 0  &&
                                       (questionNode \ "questionId").text.trim.length > 0 &&
                                       (questionNode \ "questionId").text.trim.toInt > 0  &&
                                       (question_ \ "questionId").text.trim.toInt == (questionNode \ "questionId").text.trim.toInt) {
                                        questionList  += questionNode
                                        newEntry = false;
                                  }else{
                                        questionList  += question_
                                  }
                            }
                            if(newEntry){
                              questionList  += questionNode
                            }
                       }
                  }
              }
              case "DELETE" =>{
                  scala.xml.XML.loadFile("conf/question.xml") match {
                      case <questions>{questions @ _*}</questions> => {
                            for (question_ <- questions) {
                                  if ( (question_ \ "questionId").text.trim.length > 0 &&
                                       (question_ \ "questionId").text.trim.toInt > 0  &&
                                       (questionNode \ "questionId").text.trim.length > 0 &&
                                       (questionNode \ "questionId").text.trim.toInt > 0  &&
                                       (question_ \ "questionId").text.trim.toInt != (questionNode \ "questionId").text.trim.toInt) {
                                    questionList  += question_
                                  }
                            }
                       }
                  }
              }
        }
        scala.xml.XML.save("conf/question.xml", <questions>{for(question <- questionList) yield question}</questions>, "UTF-8", false, null)
    }
}