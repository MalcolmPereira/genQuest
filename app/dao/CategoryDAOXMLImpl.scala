package dao

import java.util.concurrent.atomic.AtomicLong

import model.Category

import scala.collection.mutable.ListBuffer

object CategoryDAOXMLImpl extends CategoryDAO{

  private val idGenerator = new AtomicLong(
  {
      var idList       = new ListBuffer[Int]()
      val categoryNode = scala.xml.XML.loadFile("conf/category.xml")
      categoryNode match {
          case <catrgories>{catrgories @ _*}</catrgories> => {
              for (category <- catrgories) {
                  if((category \"categoryId").text.trim.length > 0 &&  (category \"categoryId").text.toInt > 0 ){
                      idList += (category \"categoryId").text.toInt
                  }
              }
          }
      }
      idList.max + 1
  })

  override def listCategories(): List[Category] = {
      var categoryList  = new ListBuffer[Category]()
      val categoryNode  = scala.xml.XML.loadFile("conf/category.xml")
      categoryNode match {
          case <catrgories>{catrgories @ _*}</catrgories> => {
            for (category <- catrgories) {
                if((category \"categoryId").text.trim.length > 0 &&  (category \"categoryId").text.toInt > 0 ){
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

  override def addCategory(category: Category): Integer = {
    null
  }

  override def deleteCategory(category: Category): Integer = {
    0
  }

  override def updateCategory(category: Category): Category = {
    null
  }

  override def findCategory(categoryId: Integer): Category = {
    null
  }
}
