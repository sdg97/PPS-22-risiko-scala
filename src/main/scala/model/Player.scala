package model

import model.PlayerColor.BLACK

enum PlayerColor(val rgb: Int) {
  case RED extends PlayerColor(0xFF0000)
  case GREEN extends PlayerColor(0x00FF00)
  case BLUE extends PlayerColor(0x0000FF)
  case YELLOW extends PlayerColor(0xFFFF00)
  case BLACK extends PlayerColor(0x000000)
  case PURPLE extends PlayerColor(0x4C0099)
}

trait Player {
  def username: String
  def color: PlayerColor
  def tanksToPlace: Int
  def setTanksToPlace(tanksNumber: Int): Unit

}


object Player:
  private class PlayerImpl(_username: String, _color: PlayerColor) extends Player {
    private var _numWagon = 0

    override def username: String = _username

    override def color: PlayerColor = _color

    override def tanksToPlace: Int = _numWagon

    override def setTanksToPlace(tanksNumber: Int): Unit = _numWagon = tanksNumber
  }

  def apply(username: String, color: PlayerColor): Player =
    new PlayerImpl(username, color)

  //da togliere o da modificare
  def apply(username: String): Player =
    new PlayerImpl(username, BLACK)

  extension (players: Set[Player])
    def START_TANK_NUMBER = players.size match
      case 3 => 35
      case 4 => 30
      case 5 => 25
      case _ => 20



