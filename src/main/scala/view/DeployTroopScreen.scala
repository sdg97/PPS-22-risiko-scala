package view

import controller.ControllerModule.*

import java.awt.{Color, Graphics, GridLayout}
import java.awt.event.ActionEvent
import java.awt.geom.{Ellipse2D, Point2D}
import javax.swing.{JButton, JPanel}
import scala.swing.{Font, Graphics2D}

private[view] object DeployTroopScreen {
  def apply(c: Controller) = {
    val panel = new JPanel(new GridLayout(0, 1))

    val button1 = new JButton() {
      //setBorder(BorderFactory.createEmptyBorder())
      setContentAreaFilled(false) // Rimuove lo sfondo del bottone
      setForeground(Color.BLUE) // Imposta il colore del testo
      setFocusPainted(false) // Rimuove l'effetto di focuss
      setText(9.toString)
      setFont(new Font("Arial", 12, 10))

      override def paintComponent(g: Graphics): Unit = {
        val g2d = g.asInstanceOf[Graphics2D]
        val center = new Point2D.Float(getWidth / 2.0f, getHeight / 2.0f)
        val radius = Math.min(getWidth, getHeight) / 2.0f
        val circle = new Ellipse2D.Float(center.x - radius, center.y - radius, 2.0f * radius, 2.0f * radius)
        g2d.setColor(Color.YELLOW) // Imposta il colore del cerchio
        g2d.fill(circle) // Disegna il cerchio
        super.paintComponent(g) // Disegna il testo del bottone
      }
    }
    button1 setBounds(38, 78, 40, 40)
    button1.addActionListener((_: ActionEvent) => {
      c.deployTroops()
    })
    panel add button1
    panel
  }
}