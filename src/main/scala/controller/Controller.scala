package controller

import view.*
import model.*
class Controller(model: Model) {

  def start() =
    View.showSettingsView(this)
  def setGameSettings() =
    model.setGameSettings()
    View.showDeploymentTroopsView(this)

  def deployTroops() =
    model.deployTroops()
    View.showGameView(this)

  def getNeighbor(stateName: String, player:Player): Set[String] = model.getNeighbor(stateName, player)
  def getPlayerStates (player: Player): Set[State] = model.getPlayerStates(player)
  def getCurrentPlayer(): Player = model.getCurrentPlayer()
}