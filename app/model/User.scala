package model

class User(val userID: Integer,
           val userName: String,
           val userPassword: String,
           val userFirstName: String,
           val userLastName: String){

  def this(userName: String,userPassword: String,userFirstName: String,userLastName: String) {
    this(0,userName,userPassword,userFirstName,userLastName)
  }

  def this(userID: Integer, userName: String,userFirstName: String,userLastName: String) {
    this(userID,userName,"",userFirstName,userLastName)
  }

  def id         = userID 
  def name  	   = userName
  def password   = userPassword
  def firstName  = userFirstName
  def lastName   = userLastName
}