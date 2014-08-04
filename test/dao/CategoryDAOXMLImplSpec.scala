package dao

import org.scalatest._
import model.Category


class CategoryDAOXMLImplSpec extends FlatSpec {

   "CategoryDAO " should " Return all categories " in {
       val categories = CategoryDAOXMLImpl.listCategories
       assert(categories!= null)
       assert(categories.size > 0 )
       for(category <- categories) {
         assert(category.id != null)
         assert(category.id > 0)
         assert(category.name != null)
         assert(category.name.length > 0)
         assert(category.description != null)
         assert(category.description.length > 0)
       }
   }

  "CategoryDAO " should " Return valid category for categoryid " in {
    val category = CategoryDAOXMLImpl.findCategory(1)
    assert(category.id != null)
    assert(category.id > 0)
    assert(category.name != null)
    assert(category.name.length > 0)
    assert(category.description != null)
    assert(category.description.length > 0)
  }

  "CategoryDAO " should " Return valid category for category name " in {
    val category = CategoryDAOXMLImpl.findCategory("java")
    assert(category.id != null)
    assert(category.id > 0)
    assert(category.name != null)
    assert(category.name.length > 0)
    assert(category.description != null)
    assert(category.description.length > 0)
  }

  "CategoryDAO " should " Return null category for invalid category name " in {
    val category = CategoryDAOXMLImpl.findCategory("invalid")
    assert(category == null)
  }

  "CategoryDAO " should " Return null for invalid categoryid " in {
    assert(CategoryDAOXMLImpl.findCategory(0) == null)
  }

  "CategoryDAO " should " add/update and delete new category " in {
    val categoryID = CategoryDAOXMLImpl.addCategory(new Category("newcategoryName","newcategoryDesc"))
    assert( categoryID > 0)
    val category = CategoryDAOXMLImpl.updateCategory(new Category(categoryID,"updated category desc"))
    assert( category != null)
    val rowid = CategoryDAOXMLImpl.deleteCategory(category)
    assert( rowid != 0 )
  }
}
