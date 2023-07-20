package model

class PlayerImpl(_username: String, _color:PlayerColor) extends Player {
  private var _numWagon = 0
  override def username: String = _username
  override def color: PlayerColor = _color
  override def wagonToPlace: Int = _numWagon
  override def setWagonToPlace(numWagon: Int): Unit = _numWagon = numWagon
}
