package dao.file

import java.util.concurrent.atomic.AtomicLong

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import dao.CategoryDAO
import model.Category
import play.api.Play.current
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka
import scala.concurrent.ExecutionContext.Implicits.global

import scala.collection.mutable.ListBuffer
import scala.xml.{Elem, Node}


object CategoryDAOXMLImpl extends CategoryDAO {

  implicit val timeout = Timeout(2.second)

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
      val f = fileManagerActor ? ManageNode(getCategoryNode(category, categoryID), "CATEGORY", "ADD")
      f.onSuccess{
        case categoryID:Integer => println ("Added New Category "+categoryID)
      }
      categoryID
  }

  override def updateCategory(category: Category): Category = {
      scala.xml.XML.loadFile("conf/category.xml") match {
          case <categories>{categories @ _*}</categories> => {
              for (category_ <- categories) {
                    if ((category_ \ "categoryId").text.trim.length > 0 && (category_ \ "categoryId").text.trim.toInt > 0 && (category_ \ "categoryId").text.trim.toInt == category.id) {
                        fileManagerActor ? ManageNode(getCategoryNode(category, category_),"CATEGORY","UPDATE")
                        return category
                    }
              }
          }
      }
      category
  }

  override def deleteCategory(categoryId: Integer): Integer = {
      var nodeCounter  = 0
      scala.xml.XML.loadFile("conf/category.xml") match {
          case <categories>{categories @ _*}</categories> => {
                for (category_ <- categories) {
                  if ((category_ \ "categoryId").text.trim.length > 0 && (category_ \ "categoryId").text.trim.toInt > 0 && (category_ \ "categoryId").text.trim.toInt == categoryId) {
                    fileManagerActor ? ManageNode(category_,"CATEGORY","DELETE")
                    nodeCounter  = nodeCounter  + 1
                    return nodeCounter
                }
            }
          }
    }
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