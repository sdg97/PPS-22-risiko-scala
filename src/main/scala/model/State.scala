package model

trait State {
  def name: String
  def numberOfWagon: Int
  def player: Player
  def addWagon(numberOfWagonsToAdd: Int): Unit
  def removeWagon(numberOfWagonToRemove: Int): Unit
}
