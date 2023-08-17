package view

import controller.ControllerModule
import view.screen.{GameScreen, SettingsScreen}
import view.window.GameWindow

/**
 * The module that represent the View
 */
object ViewModule:

  /**
   * The application user interface
   */
  trait View:

    /**
     * Show the user interface
     */
    def show() : Unit

    /**
     * Show the settings view interface
     */
    def showSettingsView() : Unit

    /**
     * Show the game view interface
     */
    def showGameView() : Unit

    /**
     * Force the update of the current screen
     */
    def update(): Unit

  /**
   * What the module provide to the other module
   */
  trait Provider:
    val view: View

  /**
   * The requirements necessary for the functioning of the module
   */
  type Requirements = ControllerModule.Provider

  /**
   * The module component used by the provider
   */
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

  /**
   * Module interface for mixing the module with the others MVC modules
   */
  trait Interface extends Provider with Component:
    self: Requirements =>
