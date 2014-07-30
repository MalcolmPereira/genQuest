package util

import org.mindrot.jbcrypt.BCrypt;

object BCryptUtil {
  def create(str: String) :String = {
     BCrypt.hashpw(str, BCrypt.gensalt());
  }
  def check(str1: String, str2: String) :Boolean = {
     BCrypt.checkpw(str1, str2);
  }
}