package dao

import java.util.concurrent.atomic.AtomicLong

import akka.actor.Props
import model.Category

import scala.collection.mutable.ListBuffer

import scala.xml.Elem
import scala.xml.Node
import play.api.libs.concurrent.Akka
import play.api.Play.current

object CategoryDAOXMLImpl extends CategoryDAO {

  val fileManagerActor = Akka.system.actorOf(Props[FileManagerActor])

  private val idGenerator = new AtomicLong(
  {
    var idList = new ListBuffer[Int]()
    scala.xml.XML.loadFile("conf/category.xml") match {
      case <categories>{categories @ _*}</categories> => {
        for (category <- categories) {
          if ((category \ "categoryId").text.trim.length > 0 && (category \ "categoryId").text.trim.toInt > 0) {
            idList += (category \ "categoryId").text.toInt
          }
        }
      }
    }
    idList.max + 1
  })

  override def listCategories(): List[Category] = {
    var categoryList = new ListBuffer[Category]()
    scala.xml.XML.loadFile("conf/category.xml") match {
      case <categories>{categories @ _*}</categories> => {
        for (category <- categories) {
          if ((category \ "categoryId").text.trim.length > 0 && (category \ "categoryId").text.trim.toInt > 0) {
            categoryList += getCategory(category)
          }
        }
      }
    }
    categoryList.toList
  }

  override def findCategory(categoryId: Integer): Category = {
    scala.xml.XML.loadFile("conf/category.xml") match {
      case <categories>{categories @ _*}</categories> => {
        for (category <- categories) {
          if ((category \ "categoryId").text.trim.length > 0 && (category \ "categoryId").text.trim.toInt > 0 && (category \ "categoryId").text.trim.toInt == categoryId) {
            return getCategory(category)
          }
        }
      }
    }
    null
  }

  override def findCategory(categoryName: String): Category = {
    scala.xml.XML.loadFile("conf/category.xml") match {
      case <categories>{categories @ _*}</categories> => {
        for (category <- categories) {
          if ((category \ "categoryId").text.trim.length > 0 && (category \ "categoryId").text.trim.toInt > 0 && (category \ "categoryName").text.trim.equalsIgnoreCase(categoryName)) {
            return getCategory(category)
          }
        }
      }
    }
    null
  }



  override def addCategory(category: Category): Integer = {
    val categoryID = idGenerator.getAndIncrement.toInt
    val categoryNodeUpdated = scala.xml.XML.loadFile("conf/category.xml") match {
      case Elem(prefix, label, attribs, scope, child @ _*) => {
        Elem(prefix, label, attribs, scope, true, child ++ getCategoryNode(category,categoryID): _*)
      }
    }
    fileManagerActor ! CategoryAddNode(categoryNodeUpdated)
    categoryID
  }

  override def updateCategory(category: Category): Category = {
    var categoryList  = new ListBuffer[Node]()
    scala.xml.XML.loadFile("conf/category.xml") match {
      case <categories>{categories @ _*}</categories> => {

        for (category_ <- categories) {
             if ((category_ \ "categoryId").text.trim.length > 0 && (category_ \ "categoryId").text.trim.toInt > 0 && (category_ \ "categoryId").text.trim.toInt == category.id) {
                 categoryList  += getCategoryNode(category, category_)
             }else{
                 categoryList  += category_
             }

          }

      }
    }
    fileManagerActor ! CategoryNode(categoryList.toList)
    category
  }

  override def deleteCategory(categoryId: Integer): Integer = {
    var categoryList  = new ListBuffer[Node]()
    var nodeCounter  = 0
    scala.xml.XML.loadFile("conf/category.xml") match {
      case <categories>{categories @ _*}</categories> => {
        for (category_ <- categories) {
          if ((category_ \ "categoryId").text.trim.length > 0 && (category_ \ "categoryId").text.trim.toInt > 0 && (category_ \ "categoryId").text.trim.toInt != categoryId) {
            categoryList  += category_
          }else{
            nodeCounter  = nodeCounter  + 1
          }
        }
      }
    }
    fileManagerActor ! CategoryNode(categoryList.toList)
    nodeCounter
  }

  private def getCategory(category: Node):Category = {
    new Category((category \ "categoryId").text.toInt,
      (category \ "categoryName").text.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("quot;","\""),
      (category \ "categoryDescription").text.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("quot;","\"")
    )
  }

  private def getCategoryNode(category: Category, categoryID: Int) :Node = {
    scala.xml.XML.loadString(
        "<category><categoryId>" + categoryID +
        "</categoryId><categoryName>" + category.name.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","quot;") +
        "</categoryName><categoryDescription>" + category.description.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","quot;") +
        "</categoryDescription></category>"
    )
  }

  private def getCategoryNode(category: Category, categoryNode: Node) :Node = {
    scala.xml.XML.loadString(
      "<category><categoryId>" + (categoryNode \ "categoryId").text +
        "</categoryId><categoryName>" + (categoryNode \ "categoryName").text +
        "</categoryName><categoryDescription>" + category.description.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;").replace("\"","quot;") +
        "</categoryDescription></category>"
    )
  }
}