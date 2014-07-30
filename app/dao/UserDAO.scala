package dao
import model.User

trait UserDAO {
    def listUsers(): List[User]
    def findUser(userName: String, userPassword: String): User
    def findUser(userName: String): User
    def findUser(userId: Integer): User
    def addUser(user: User): Long
    def updateUser(user: User): User
    //def deleteUser(user: User): Int
}
