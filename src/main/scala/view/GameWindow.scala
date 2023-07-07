package view

import java.awt.BorderLayout
import javax.swing.{JFrame, JPanel}

class GameWindow {
  private val frame = new JFrame("GUI con sfondo")

  def show() =
    // Imposta le dimensioni del frame e visualizzalo
    frame.pack()
    frame.setVisible(true)

  def changeScreen(p: JPanel) =
    frame.getContentPane.removeAll()
    frame.getContentPane.add(p, BorderLayout.CENTER)
    frame.getContentPane.doLayout()
    frame.update(frame.getGraphics())
}
