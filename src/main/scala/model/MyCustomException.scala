package model

class MyCustomException (message: String) extends Exception{
  override def getMessage: String = message
}
