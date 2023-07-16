package view

import javax.swing.JButton
import scala.swing.Color

class JButtonExtended(text: String, c: Color) extends JButton(text) {
  private var _isNeighbour: Boolean = false
  private var _color: Color = c

  def setIsNeighbour(value: Boolean): Unit = {
    _isNeighbour = value
  }

  def isNeighbour: Boolean = {
    _isNeighbour
  }

  def color: Color = _color
  def setColor(c: Color) = _color = c
}