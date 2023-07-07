package controller

import view.*
import model.*
class Controller(model: ModelImpl) {

  def start() =
    View.showSettingsView(this)
  def setGameSettings(inputDataPlayer: Set[(String, Int)]) =
    model.setGameSettings(inputDataPlayer: Set[(String, Int)])
    View.showDeploymentTroopsView(this)

  def deployTroops() =
    model.deployTroops()
    View.showGameView(this)
}