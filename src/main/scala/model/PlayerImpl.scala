package model

class PlayerImpl(_username: String, _color:Color) extends Player {
  override def username: String = _username

  override def color: Color = _color
  
}
