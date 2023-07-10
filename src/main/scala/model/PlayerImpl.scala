package model

class PlayerImpl(_username: String, _color:PlayerColor) extends Player {
  override def username: String = _username
  override def color: PlayerColor = _color
}
