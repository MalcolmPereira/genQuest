package model

class User(userID: Integer,
           userName: String,
           userFirstName: String,
           userLastName: String){
  def id         = userID 
  def name  	 = userName
  def firstName  = userFirstName
  def lastName   = userLastName
}