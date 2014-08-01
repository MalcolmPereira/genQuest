package dao

import java.util.concurrent.atomic.AtomicLong

import model.Category

import scala.collection.mutable.ListBuffer

import scala.xml.Elem
import scala.xml.Node

object CategoryDAOXMLImpl extends CategoryDAO {

  private val idGenerator = new AtomicLong(
  {
    var idList = new ListBuffer[Int]()
    val categoryNode = scala.xml.XML.loadFile("conf/category.xml")
    categoryNode match {
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
    val categoryNode = scala.xml.XML.loadFile("conf/category.xml")
    categoryNode match {
      case <categories>{categories @ _*}</categories> => {
        for (category <- categories) {
          if ((category \ "categoryId").text.trim.length > 0 && (category \ "categoryId").text.trim.toInt > 0) {
            categoryList += new Category((category \ "categoryId").text.toInt,
              (category \ "categoryName").text,
              (category \ "categoryDescription").text
            )
          }
        }
      }
    }
    categoryList.toList
  }

  override def findCategory(categoryId: Integer): Category = {
    val categoryNode = scala.xml.XML.loadFile("conf/category.xml")
    categoryNode match {
      case <categories>{categories @ _*}</categories> => {
        for (category <- categories) {
          if ((category \ "categoryId").text.trim.length > 0 && (category \ "categoryId").text.trim.toInt > 0 && (category \ "categoryId").text.trim.toInt == categoryId) {
            return new Category((category \ "categoryId").text.toInt,
              (category \ "categoryName").text,
              (category \ "categoryDescription").text
            )
          }
        }
      }
    }
    null
  }

  override def addCategory(category: Category): Integer = {
    val categoryID = idGenerator.getAndIncrement.toInt
    val nodeString = "<category><categoryId>" + categoryID +
                     "</categoryId><categoryName>" + category.name +
                     "</categoryName><categoryDescription>" + category.description +
                     "</categoryDescription></category>"

    val nodeXML = scala.xml.XML.loadString(nodeString)
    val categoryNode = scala.xml.XML.loadFile("conf/category.xml")

    val categoryNodeUpdated = categoryNode match {
      case Elem(prefix, label, attribs, scope, child @ _*) => {
        Elem(prefix, label, attribs, scope, true, child ++ nodeXML: _*)
      }
    }

    scala.xml.XML.save("conf/category.xml", categoryNodeUpdated, "UTF-8", false, null)
    categoryID
  }

  override def updateCategory(category: Category): Category = {
    val categoryNode  = scala.xml.XML.loadFile("conf/category.xml")
    var categoryList  = new ListBuffer[Node]()
    categoryNode match {
      case <categories>{categories @ _*}</categories> => {

        for (category_ <- categories) {
             if ((category_ \ "categoryId").text.trim.length > 0 && (category_ \ "categoryId").text.trim.toInt > 0 && (category_ \ "categoryId").text.trim.toInt == category.id) {
                 categoryList  += {
                    scala.xml.XML.loadString(
                        "<category><categoryId>" + (category_ \ "categoryId").text +
                        "</categoryId><categoryName>" + (category_ \ "categoryName").text +
                        "</categoryName><categoryDescription>" + category.description +
                        "</categoryDescription></category>"
                    )
                 }
             }else{
                 categoryList  += category_
             }

          }

      }
    }
    val categoryNodeUpdated = <categories>{for(category <- categoryList) yield category}</categories>
    scala.xml.XML.save("conf/category.xml", categoryNodeUpdated, "UTF-8", false, null)
    category
  }

  override def deleteCategory(category: Category): Integer = {
    val categoryNode  = scala.xml.XML.loadFile("conf/category.xml")
    var categoryList  = new ListBuffer[Node]()
    var nodeCounter  = 0
    categoryNode match {
      case <categories>{categories @ _*}</categories> => {

        for (category_ <- categories) {
          if ((category_ \ "categoryId").text.trim.length > 0 && (category_ \ "categoryId").text.trim.toInt > 0 && (category_ \ "categoryId").text.trim.toInt != category.id) {
            categoryList  += category_
          }else{
            nodeCounter  = nodeCounter  + 1
          }
        }

      }
    }
    val categoryNodeUpdated = <categories>{for(category <- categoryList) yield category}</categories>
    scala.xml.XML.save("conf/category.xml", categoryNodeUpdated, "UTF-8", false, null)
    nodeCounter
  }
}
