package model

class Category(categoryId: Integer,
           categoryName: String,
           categoryDescription: String){
  def id           = categoryId 
  def name  	   = categoryName
  def description  = categoryDescription
}