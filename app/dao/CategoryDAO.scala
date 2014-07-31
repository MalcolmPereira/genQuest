package dao
import model.Category

trait CategoryDAO {
  def listCategories(): List[Category]
  def findCategory(categoryId: Integer): Category
  def addCategory(category: Category): Integer
  def updateCategory(category: Category): Category
  def deleteCategory(category: Category): Integer
}
