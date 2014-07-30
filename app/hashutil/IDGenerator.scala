package hashutil

object IDGenerator {
  private val n = new java.util.concurrent.atomic.AtomicLong(getLastUserID())
  
  def next = n.getAndIncrement()
  
  def getLastUserID() : Long = {
      val loginNode = scala.xml.XML.loadFile("conf/login.xml")
 	  
	  loginNode match {
 	    case <users>{users @ _*}</users> => {
			var idList = List(0)
 	    	for (user <- users) {
  			  if((user \"userID").text.trim.length > 0 &&  (user \"userID").text.toInt > 0 ){
				  idList =  idList :+ (user \"userID").text.toInt
			  }
			}
			idList.max + 1
 	    }
 	 }
  }
}