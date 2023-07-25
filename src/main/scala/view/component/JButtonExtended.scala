package view.component

import java.awt.geom.{Ellipse2D, Point2D}
import java.awt.{Color, Font, Graphics, Graphics2D}
import javax.swing.{BorderFactory, JButton}
import scala.swing.Color

class JButtonExtended(posX: Int, posY: Int) extends JButton() {
  private var _isNeighbour: Boolean = false
  private var _color: Color = Color.DARK_GRAY

  def setIsNeighbour(value: Boolean): Unit = {
    _isNeighbour = value
  }
  def isNeighbour: Boolean = {
    _isNeighbour
  }
  def color: Color = _color
  def setColor(c: Color) = _color = c

  setBorder(BorderFactory.createEmptyBorder())
  setContentAreaFilled(false) // Rimuove lo sfondo del bottone
  setFocusPainted(false) // Rimuove l'effetto di focuss
  setFont(new Font("Arial", 12, 10))
  setRolloverEnabled(true)

  override def paintComponent(g: Graphics): Unit = {
    val g2d = g.asInstanceOf[Graphics2D]
    val center = new Point2D.Float(getWidth / 2.0f, getHeight / 2.0f)
    val radius = Math.min(getWidth, getHeight) / 2.0f
    val circle = new Ellipse2D.Float(center.x - radius, center.y - radius, 2.0f * radius, 2.0f * radius)
    g2d.setColor(this.color) // Imposta il colore del cerchio
    g2d.fill(circle) // Disegna il cerchio

    val bgColor = new Color(this.color.getRGB)
    val luminance = (0.299 * bgColor.getRed + 0.587 * bgColor.getGreen + 0.114 * bgColor.getBlue) / 255.0
    if (luminance > 0.5)
      setForeground(Color.BLACK)
    else
      setForeground(Color.WHITE)
    super.paintComponent(g) // Disegna il testo del bottone
  }
  setBounds(posX, posY, 40, 40)
}