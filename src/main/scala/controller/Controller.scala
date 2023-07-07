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
}