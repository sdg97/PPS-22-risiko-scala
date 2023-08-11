package view

import controller.ControllerModule
import view.screen.{GameScreen, SettingsScreen}
import view.window.GameWindow

object ViewModule:
  trait View:
    def show() : Unit
    def showSettingsView() : Unit
    def showGameView() : Unit
    def update(): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:
      private var gw : Option[GameWindow] = None
      def show() =
        gw = Some(new GameWindow(controller))
        gw.get.show()
      def showSettingsView() = gw.get changeScreen SettingsScreen(context.controller)

      def showGameView() = gw.get changeScreen GameScreen(context.controller)
      def update() = GameScreen.update()

  trait Interface extends Provider with Component:
    self: Requirements =>
