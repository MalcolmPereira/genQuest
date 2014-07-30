package dao

import model.User
import util._
import scala.collection.mutable.ListBuffer
import java.util.concurrent.atomic.AtomicLong
import scala.xml.Elem
import scala.xml.Node

object UserDAOXMLImpl extends UserDAO {

  private val idGenerator = new AtomicLong(
      {
          var idList  = new ListBuffer[Int]()
          val loginNode = scala.xml.XML.loadFile("conf/login.xml")
          loginNode match {
              case <users>{users @ _*}</users> => {
                  for (user <- users) {
                      if((user \"userID").text.trim.length > 0 &&  (user \"userID").text.toInt > 0 ){
                          idList += (user \"userID").text.toInt
                      }
                  }
              }
          }
          idList.max + 1
    }
  )

  override def listUsers : List[User] = {
      var userList  = new ListBuffer[User]()
      val loginNode = scala.xml.XML.loadFile("conf/login.xml")
      loginNode match {
          case <users>{users @ _*}</users> => {
              for (user <- users) {
                  if ((user \ "userID").text.trim.length > 0 && (user \ "userID").text.length > 0) {
                    userList += new User((user \ "userID").text.toInt,
                                         (user \ "userName").text,
                                         (user \ "userPassword").text,
                                         (user \ "userFirstName").text,
                                         (user \ "userLastName").text
                                        )
                  }
              }
          }
      }
      userList.toList
  }

  override def findUser(userName: String, userPassword: String): User = {
      val loginNode = scala.xml.XML.loadFile("conf/login.xml")
      loginNode match {
          case <users>{users @ _*}</users> => {
              for (user <- users) {
                   if((user \"userName").text == userName && BCryptUtil.check(userPassword,(user \"userPassword").text)){
                       return new User((user \"userID").text.toInt,
                                       (user \"userName").text,
                                       (user \ "userPassword").text,
                                       (user \"userFirstName").text,
                                       (user \"userLastName").text
                                       )
                    }
              }
          }
          null
      }
  }

  override def findUser(userName: String): User = {
      val loginNode = scala.xml.XML.loadFile("conf/login.xml")
      loginNode match {
          case <users>{users @ _*}</users> => {
              for (user <- users) {
                  if((user \"userName").text == userName){
                       return new User((user \"userID").text.toInt,
                                       (user \"userName").text,
                                       (user \ "userPassword").text,
                                       (user \"userFirstName").text,
                                       (user \"userLastName").text
                                      )
                  }
              }
          }
          null
      }
  }

  override def findUser(userId: Integer): User = {
      val loginNode = scala.xml.XML.loadFile("conf/login.xml")
      loginNode match {
          case <users>{users @ _*}</users> => {
              for (user <- users) {
                if((user \"userID").text.trim.length > 0 &&  (user \"userID").text.trim.toInt == userId){
                        return new User((user \"userID").text.toInt,
                                        (user \"userName").text,
                                        (user \ "userPassword").text,
                                        (user \"userFirstName").text,
                                        (user \"userLastName").text
                                        )
                   }
              }
          }
          null
      }
  }

  override def addUser(user: User): Long = {
      val userID       = idGenerator.getAndIncrement
      val nodeString   = "<user><userID>"+userID+
                         "</userID><userName>"+user.name+
                         "</userName><userPassword>"+BCryptUtil.create(user.password)+
                         "</userPassword><userFirstName>"+user.firstName+
                         "</userFirstName><userLastName>"+user.lastName+
                         "</userLastName></user>"
      val nodeXML           = scala.xml.XML.loadString(nodeString)
      val loginNode         = scala.xml.XML.loadFile("conf/login.xml")
      val loginNodeUpdated  = addChild(loginNode, nodeXML)
      scala.xml.XML.save("conf/login.xml", loginNodeUpdated, "UTF-8", false, null)
      userID
  }

  def addChild(n: Node, newChild: Node) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) =>
      Elem(prefix, label, attribs, scope, true, child ++ newChild : _*)
  }

  //override def deleteUser(user: User): Int = ???

  //override def updateUser(user: User): Int = ???
}
