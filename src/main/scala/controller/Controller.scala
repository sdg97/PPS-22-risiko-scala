package controller

import view.*
import model.*
class Controller(model: ModelImpl) {

  def start() =
    View.showSettingsView(this)
  def setGameSettings(inputDataPlayer: Set[(String, String)]) =
    model.setGameSettings(inputDataPlayer: Set[(String, String)])
    model.getSetOfPlayers().foreach(player=>println(player.username+", "+player.color.toString))
    View.showDeploymentTroopsView(this)

  def deployTroops() =
    model.deployTroops()
    View.showGameView(this)

  def getNeighbor(stateName: String, player:Player): Set[String] = model.getNeighbor(stateName, player)
  def getPlayerStates (player: Player): Set[State] = model.getPlayerStates(player)
  def getCurrentPlayer(): Player = model.getCurrentPlayer()
}