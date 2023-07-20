package model

trait State {
  def name: String
  def numberOfWagon: Int
  def player: Player
  def posX: Int
  def posY: Int
  def addWagon(numberOfWagonsToAdd: Int): Unit
  def removeWagon(numberOfWagonToRemove: Int): Unit
  def setPlayer(p: Player): Unit
}

object State:
  def apply(name: String, numberOfWagon: Int, player: Player, posX: Int, posY: Int): State =
    new StateImpl(name, numberOfWagon, player, posX, posY)

