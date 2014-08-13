package model

case class Category(val categoryId: Integer,val categoryName: String,val categoryDescription: String){

  def this(categoryName: String, categoryDescription: String) {
    this(0,categoryName,categoryDescription)
  }

  def this(categoryId: Integer, categoryDescription: String) {
    this(categoryId,"",categoryDescription)
  }

  def id           = categoryId 
  def name  	   = categoryName
  def description  = categoryDescription
}