package view
import controller.*

import java.awt.event.ActionEvent
import java.awt.geom.{Ellipse2D, Point2D}
import java.awt.{Color, Graphics, GridLayout}
import javax.swing.JOptionPane.*
import javax.swing.*
import scala.swing.{Font, Graphics2D}

/**
 * View for setup simulation.
 */
private[view] object SettingsScreen {
  def apply(c: Controller) = {
    val panel = new JPanel(new GridLayout(0, 1))

    /**
     *
      panel.add(new JLabel("N° Colonies (queens):"))
      val numColonies = new JTextField("2")
      panel.add(numColonies)
    */

      //la view deve fare inserire dei dati simili agli utenti
      //lista dei giocatori e colore assegnato
      // non è una scelta definitiva ma è per far partire il gioco in maniera ciclica


    val button1 = new JButton() {
      //setBorder(BorderFactory.createEmptyBorder())
      setContentAreaFilled(false) // Rimuove lo sfondo del bottone
      setForeground(Color.BLACK) // Imposta il colore del testo
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
    button1.setBounds(38, 78, 40, 40)
    button1.addActionListener((_: ActionEvent) => {
      val players = ("Martin", 0xFF0000) :: ("Pietro", 0x00FF00) :: ("Simone", 0x0000FF) :: Nil
      val mapIndex = 1
      c.setGameSettings()
    })
    panel add button1
    panel
  }

  /**
   * Interessante converte le stringhe in intero, utile per non doverlo specificare tutte
   * le volte che prendo in ingresso qualcosa dall'interfaccia grafica
   * occhio che in scala 3 non si fanno così gli impliciti xD
  private implicit def numberFrom(component: JTextField): Int = component.getText toInt

  private implicit def numberFrom[T](component: JComboBox[T]): Int = component.getSelectedItem.toString toInt
   */
}
