package dao
import model.Category

trait CategoryDAO {
  def listCategories(): List[Category]
  def findCategory(categoryId: Integer): Category
  def findCategory(categoryName: String): Category
  def addCategory(category: Category): Integer
  def updateCategory(category: Category): Category
  def deleteCategory(category: Category): Integer
}
