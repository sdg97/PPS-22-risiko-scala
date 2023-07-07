package model

trait Model {
  def setGameSettings(inputDataPlayer: Set[(String,Int)]):Unit
  def getSetOfPlayers():Set[Player]
  def deployTroops():Unit
}
