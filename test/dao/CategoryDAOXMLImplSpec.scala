package dao

import org.scalatest._
import model.Category


class CategoryDAOXMLImplSpec extends FlatSpec {

   "CategoryDAO " should " Return all categories " in {
    assert(CategoryDAOXMLImpl.listCategories != null)
    assert(CategoryDAOXMLImpl.listCategories.size > 0 )
  }


}
