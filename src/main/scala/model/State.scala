package model

trait State {
  def name: String
  def numberOfWagon: Int
  def addWagon(numberOfWagonsToAdd: Int): Unit
  def removeWagon(numberOfWagonToRemove: Int): Unit
}
