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

object M extends App:
  Player("simone", PlayerColor.BLACK)

