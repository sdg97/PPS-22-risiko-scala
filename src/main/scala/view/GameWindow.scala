package view

import view.GameScreen.getClass

import java.awt.BorderLayout
import javax.swing.{JFrame, JPanel, WindowConstants}

class GameWindow {
  private val TITILE = "Risiko"
  private val ICON_PATH = "/map_ref.png"
  private val frame = new JFrame(TITILE)

  def show() =
    // Imposta le dimensioni del frame e visualizzalo
    frame.setIconImage(javax.imageio.ImageIO.read(getClass.getResourceAsStream(ICON_PATH)))
    frame.pack()
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setVisible(true)
    frame.setResizable(false)

  def changeScreen(p: JPanel) =
    frame.getContentPane.removeAll()
    frame.getContentPane.add(p, BorderLayout.CENTER)
    frame.pack()
    /**
    frame.getContentPane.doLayout()
    frame.update(frame.getGraphics())
    */
}
