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
      val loginNodeUpdated  = loginNode match {
        case Elem(prefix, label, attribs, scope, child @ _*) => {
          Elem(prefix, label, attribs, scope, true, child ++ nodeXML: _*)
        }
      }
      scala.xml.XML.save("conf/login.xml", loginNodeUpdated, "UTF-8", false, null)
      userID
  }

  override def updateUser(user: User): User = {
      val loginNode = scala.xml.XML.loadFile("conf/login.xml")
      var userList  = new ListBuffer[Node]()
      loginNode match {
          case <users>{users @ _*}</users> => {
                for (user_ <- users) {
                    if((user_ \"userID").text.trim.length > 0 &&  (user_ \"userID").text.toInt > 0 && (user_ \"userID").text.toInt == user.id){
                      userList  += {
                                    scala.xml.XML.loadString(
                                      "<user><userID>" + (user_ \"userID").text +
                                      "</userID><userName>" + (user_ \"userName").text +
                                      "</userName><userPassword>" + (user_ \"userPassword").text +
                                      "</userPassword><userFirstName>" + user.firstName +
                                      "</userFirstName><userLastName>" + user.lastName +
                                      "</userLastName></user>")
                                   }
                    }else{
                        userList  += user_
                    }
                }
          }
      }
      val loginNodeUpdated = <users>{for(user <- userList) yield user}</users>
      scala.xml.XML.save("conf/login.xml", loginNodeUpdated, "UTF-8", false, null)
      user
  }

  override def deleteUser(user: User): Int = {
      val loginNode    = scala.xml.XML.loadFile("conf/login.xml")
      var userList     = new ListBuffer[Node]()
      var nodeCounter  = 0;
      loginNode match {
        case <users>{users @ _*}</users> => {
          for (user_ <- users) {
              if((user_ \"userID").text.trim.length > 0 &&  (user_ \"userID").text.toInt > 0 && (user_ \"userID").text.toInt != user.id){
                userList  += user_
              }else{
                nodeCounter  = nodeCounter  + 1
              }
          }
        }
      }
      val loginNodeUpdated = <users>{for(user <- userList) yield user}</users>
      scala.xml.XML.save("conf/login.xml", loginNodeUpdated, "UTF-8", false, null)
      nodeCounter
  }


}
