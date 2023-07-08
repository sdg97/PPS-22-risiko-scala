package model

trait Model {
  def setGameSettings(inputDataPlayer: Set[(String,Int)]):Unit
  def getSetOfPlayers():Set[Player]
  def deployTroops():Unit
  def getNeighbor(stateName: String, player: Player): Set[String]
  def getPlayerStates(player: Player): Set[State]
  def getCurrentPlayer(): Player
}
