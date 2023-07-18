package view

import java.awt.{Color, Graphics, Graphics2D}
import javax.swing.JComponent

class DadoComponent(typeOfPlayer:String) extends JComponent{
  private var value: Int = 1

  def setValue(newValue: Int): Unit = {
    value = newValue
    repaint()
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)

    val g2d = g.asInstanceOf[Graphics2D]

    // Imposta il colore di riempimento del dado
    if(typeOfPlayer=="attacker"){
      g2d.setColor(Color.RED)
    }
    else{
      g2d.setColor(new Color(0X409fff))
    }

    g2d.fillRect(10, 10, 50,50)

    // Imposta il colore dei punti
    g2d.setColor(Color.BLACK)

    // Disegna i punti del dado in base al valore
    val pointSize = Math.min(getWidth, getHeight) / 8
    val pointSpacing = Math.min(getWidth, getHeight) / 12
    val pointOffset = (getHeight - (3 * pointSize + 2 * pointSpacing)) / 2

    value match {
      case 1 =>
          g2d.fillOval(26, 26, pointSize, pointSize)
        case 2 =>
          g2d.fillOval(15, 15, pointSize, pointSize)
          g2d.fillOval(40, 40, pointSize, pointSize)
        case 3 =>
          g2d.fillOval(15, 15, pointSize, pointSize)
          g2d.fillOval(28, 28, pointSize, pointSize)
          g2d.fillOval(40, 40, pointSize, pointSize)
        case 4 =>
          g2d.fillOval(15, 15, pointSize, pointSize)
          g2d.fillOval(40, 15, pointSize, pointSize)
          g2d.fillOval(15, 40, pointSize, pointSize)
          g2d.fillOval(40, 40, pointSize, pointSize)
        case 5 =>
          g2d.fillOval(15, 15, pointSize, pointSize)
          g2d.fillOval(40, 15, pointSize, pointSize)
          g2d.fillOval(28, 28, pointSize, pointSize)
          g2d.fillOval(15, 40, pointSize, pointSize)
          g2d.fillOval(40, 40, pointSize, pointSize)
        case 6 =>
          g2d.fillOval(15, 15, pointSize, pointSize)
          g2d.fillOval(40, 15, pointSize, pointSize)
          g2d.fillOval(15, 28, pointSize, pointSize)
          g2d.fillOval(40, 28, pointSize, pointSize)
          g2d.fillOval(15, 40, pointSize, pointSize)
          g2d.fillOval(40, 40, pointSize, pointSize)
        case _ =>
    }
  }
}
