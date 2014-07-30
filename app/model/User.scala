package model

class User(userID: Long,
           userName: String,
           userPassword: String,
           userFirstName: String,
           userLastName: String){
  def id         = userID 
  def name  	   = userName
  def password   = userPassword
  def firstName  = userFirstName
  def lastName   = userLastName
}