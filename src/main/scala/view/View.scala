package view

import controller.ControllerModule

object ViewModule:
  trait View:
    def show() : Unit
    def showSettingsView() : Unit
    def showDeploymentTroopsView() : Unit
    def showGameView() : Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:
      private val gw = new GameWindow(controller)
      def show() = gw.show()
      def showSettingsView() = gw changeScreen SettingsScreen(context.controller)
      def showDeploymentTroopsView() = gw changeScreen DeployTroopScreen(context.controller)
      def showGameView() = gw changeScreen GameScreen(context.controller)

  trait Interface extends Provider with Component:
    self: Requirements =>
