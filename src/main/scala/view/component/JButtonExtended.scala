package view.component

import model.entity.map.Position
import view.window.MyButtonClickEvent

import java.awt.geom.{Ellipse2D, Point2D}
import java.awt.{Color, Font, Graphics, Graphics2D}
import javax.swing.{BorderFactory, JButton}
import scala.collection.mutable.ListBuffer
import scala.swing.Color

class JButtonExtended(position: Position) extends JButton() {
  private var _isNeighbour: Boolean = false
  private var _color: Color = Color.DARK_GRAY

  /**
   * method for setting whether the current button is bordering the selected button
   * @param value the boolean to set if the button is neighbor or not
   */
  def setIsNeighbour(value: Boolean): Unit =
    _isNeighbour = value

  /**
   *
   * @return true if the button is bordering the selected button
   */
  def isNeighbour: Boolean =
    _isNeighbour

  /**
   *
   * @return the color of the button
   */
  def color: Color = _color

  /**
   * method to set a color to a button
   * @param c the color to set the button
   */
  def setColor(c: Color): Unit = _color = c

  setBorder(BorderFactory.createEmptyBorder())
  setContentAreaFilled(false)
  setFocusPainted(false)
  setFont(new Font("Arial", 12, 10))
  setRolloverEnabled(true)

  override def paintComponent(g: Graphics): Unit = {
    val g2d = g.asInstanceOf[Graphics2D]
    val center = new Point2D.Float(getWidth / 2.0f, getHeight / 2.0f)
    val radius = Math.min(getWidth, getHeight) / 2.0f
    val circle = new Ellipse2D.Float(center.x - radius, center.y - radius, 2.0f * radius, 2.0f * radius)
    g2d.setColor(this.color)
    g2d.fill(circle)

    //calculate luminance to set the foreground color
    val bgColor = new Color(this.color.getRGB)
    val luminance = (0.299 * bgColor.getRed + 0.587 * bgColor.getGreen + 0.114 * bgColor.getBlue) / 255.0
    if (luminance > 0.5)
      setForeground(Color.BLACK)
    else
      setForeground(Color.WHITE)
    super.paintComponent(g)
  }
  setBounds(position._1, position._2, 40, 40)
}