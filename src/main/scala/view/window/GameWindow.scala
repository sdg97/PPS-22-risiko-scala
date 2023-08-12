package view.window

import controller.Controller
import controller.ControllerModule.*

import java.awt.BorderLayout
import javax.swing.{JFrame, JPanel, WindowConstants}

private[view] class GameWindow(private val c: Controller) {
  private val TITLE = "Risiko"
  private val ICON_PATH = "/map_ref.png"
  private val frame = new JFrame(TITLE)

  def show() =
    // Imposta le dimensioni del frame e visualizzalo
    frame.setIconImage(javax.imageio.ImageIO.read(getClass.getResourceAsStream(ICON_PATH)))
    frame.pack()
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setVisible(true)
    frame.setResizable(false)
    c.startNewGame()

  def changeScreen(p: JPanel) =
    frame.getContentPane.removeAll()
    frame.getContentPane.add(p, BorderLayout.CENTER)
    frame.pack()
    /**
    frame.getContentPane.doLayout()
    frame.update(frame.getGraphics())
    */
}
