package view

import controller.*

/**
 * Implementation of View.
 */
object View {
  private val gw = new GameWindow()
  def start() = gw.show()

  def showSettingsView(c: Controller) = gw changeScreen SettingsScreen(c)

  def showDeploymentTroopsView(c: Controller) = gw changeScreen DeployTroopScreen(c)
  def showGameView(c: Controller) = gw changeScreen GameScreen(c)



}