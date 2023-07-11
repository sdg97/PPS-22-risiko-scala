package view

import javax.swing.JButton

class JButtonExtended(text: String) extends JButton(text) {
  private var _isNeighbour: Boolean = false

  def setIsNeighbour(value: Boolean): Unit = {
    _isNeighbour = value
  }

  def isNeighbour: Boolean = {
    _isNeighbour
  }
}