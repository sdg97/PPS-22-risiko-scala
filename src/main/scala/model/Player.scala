package model

trait Player {
  def username: String
  def color: PlayerColor
  def wagonToPlace: Int
  def setWagonToPlace(numWagon: Int): Unit

}

object Player:
  def apply(username: String, color: PlayerColor): Player =
    new PlayerImpl(username, color)
  extension (players: Set[Player])
    def START_TANK_NUMBER = players.size match
      case 3 => 35
      case 4 => 30
      case 5 => 25
      case _ => 20



