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
          scala.xml.XML.loadFile("conf/login.xml") match {
              case <users>{users @ _*}</users> => {
                  for (user <- users) {
                      if((user \"userID").text.trim.length > 0 &&  (user \"userID").text.trim.toInt > 0 ){
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
      scala.xml.XML.loadFile("conf/login.xml") match {
          case <users>{users @ _*}</users> => {
              for (user <- users) {
                  if ((user \ "userID").text.trim.length > 0 && (user \ "userID").text.trim.toInt > 0) {
                    userList += getUser(user)
                  }
              }
          }
      }
      userList.toList
  }

  override def findUser(userName: String, userPassword: String): User = {
          scala.xml.XML.loadFile("conf/login.xml") match {
          case <users>{users @ _*}</users> => {
              for (user <- users) {
                   if((user \"userName").text == userName && BCryptUtil.check(userPassword,(user \"userPassword").text)){
                       return getUser(user)
                    }
              }
          }
          null
      }
  }

  override def findUser(userName: String): User = {
     scala.xml.XML.loadFile("conf/login.xml") match {
          case <users>{users @ _*}</users> => {
              for (user <- users) {
                  if((user \"userName").text == userName){
                       return getUser(user)
                  }
              }
          }
          null
      }
  }

  override def findUser(userId: Integer): User = {
    scala.xml.XML.loadFile("conf/login.xml") match {
          case <users>{users @ _*}</users> => {
              for (user <- users) {
                if((user \"userID").text.trim.length > 0 &&  (user \"userID").text.trim.toInt == userId){
                        return getUser(user)
                   }
              }
          }
          null
      }
  }

  override def addUser(user: User): Integer = {
      val userID            = idGenerator.getAndIncrement.toInt
      val loginNodeUpdated  = scala.xml.XML.loadFile("conf/login.xml") match {
        case Elem(prefix, label, attribs, scope, child @ _*) => {
          Elem(prefix, label, attribs, scope, true, child ++ getUserNode(user,userID): _*)
        }
      }
      scala.xml.XML.save("conf/login.xml", loginNodeUpdated, "UTF-8", false, null)
      userID
  }

  override def updateUser(user: User): User = {
      var userList  = new ListBuffer[Node]()
      scala.xml.XML.loadFile("conf/login.xml") match {
          case <users>{users @ _*}</users> => {
                for (user_ <- users) {
                    if((user_ \"userID").text.trim.length > 0 &&  (user_ \"userID").text.trim.toInt > 0 && (user_ \"userID").text.trim.toInt == user.id){
                      userList  += getUserNode(user, user_)
                    }else{
                      userList  += user_
                    }
                }
          }
      }
      scala.xml.XML.save("conf/login.xml", <users>{for(user <- userList) yield user}</users>, "UTF-8", false, null)
      user
  }

  override def deleteUser(user: User): Integer = {
      var userList     = new ListBuffer[Node]()
      var nodeCounter  = 0
      scala.xml.XML.loadFile("conf/login.xml") match {
        case <users>{users @ _*}</users> => {
          for (user_ <- users) {
              if((user_ \"userID").text.trim.length > 0 &&  (user_ \"userID").text.trim.toInt > 0 && (user_ \"userID").text.trim.toInt != user.id){
                userList  += user_
              }else{
                nodeCounter  = nodeCounter  + 1
              }
          }
        }
      }
      scala.xml.XML.save("conf/login.xml", <users>{for(user <- userList) yield user}</users>, "UTF-8", false, null)
      nodeCounter
  }

  private def getUser(user: Node) :User = {
    new User((user \ "userID").text.toInt,
      (user \ "userName").text.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("quot;","\""),
      (user \ "userFirstName").text.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("quot;","\""),
      (user \ "userLastName").text.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("quot;","\"")
    )
  }

  private def getUserNode(user: User, userID: Int) :Node = {
    scala.xml.XML.loadString("<user><userID>"+userID+
      "</userID><userName>"+user.name.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","quot;")+
      "</userName><userPassword>"+BCryptUtil.create(user.password)+
      "</userPassword><userFirstName>"+user.firstName.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","quot;")+
      "</userFirstName><userLastName>"+user.lastName.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","quot;")+
      "</userLastName></user>"
    )
  }

  private def getUserNode(user: User, userNode: Node) :Node = {
    scala.xml.XML.loadString("<user><userID>" + (userNode \"userID").text +
      "</userID><userName>" + (userNode \"userName").text +
      "</userName><userPassword>" + (userNode \"userPassword").text +
      "</userPassword><userFirstName>" + user.firstName.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","quot;") +
      "</userFirstName><userLastName>" + user.lastName.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","quot;") +
      "</userLastName></user>"
    )
  }
}
