package view.window

import controller.Controller
import controller.ControllerModule.*

import java.awt.BorderLayout
import javax.swing.{JFrame, JPanel, WindowConstants}

/**
 * The principal game window
 * @param controller
 */
private[view] class GameWindow(private val controller: Controller) {
  private val TITLE = "Risiko"
  private val ICON_PATH = "/map_ref.png"
  private val frame = new JFrame(TITLE)

  /**
   * Show the game window
   */
  def show() =
    frame.setIconImage(javax.imageio.ImageIO.read(getClass.getResourceAsStream(ICON_PATH)))
    frame.pack()
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setVisible(true)
    frame.setResizable(false)
    controller.startNewGame()

  /**
   * Show a new screen
   * @param screen: the screen to show
   */
  def changeScreen(screen: JPanel) =
    frame.getContentPane.removeAll()
    frame.getContentPane.add(screen, BorderLayout.CENTER)
    frame.pack()

}
