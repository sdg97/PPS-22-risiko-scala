package view

import java.awt.Color
import javax.swing.JButton
import scala.swing.Color

class JButtonExtended(text: String) extends JButton(text) {
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
}