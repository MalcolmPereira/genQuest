package model

class Category(categoryId: Integer,
           categoryName: String,
           categoryDescription: String){

  def this(categoryName: String, categoryDescription: String) {
    this(0,categoryName,categoryDescription)
  }

  def this(categoryId: Integer, categoryDescription: String) {
    this(0,"",categoryDescription)
  }

  def id           = categoryId 
  def name  	   = categoryName
  def description  = categoryDescription
}