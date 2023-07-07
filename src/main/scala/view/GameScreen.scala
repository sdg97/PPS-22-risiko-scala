package view

import controller.Controller

import java.awt.event.ActionEvent
import java.awt.{BorderLayout, Color, Dimension, Graphics, GridLayout, Image}
import java.awt.geom.{Ellipse2D, Point2D}
import javax.swing.*
import javax.swing.JOptionPane.*
import scala.swing.{Font, Graphics2D}

/**
 * View for setup simulation.
 */
private[view] object GameScreen {
  def apply(c: Controller) =

    val image = getClass.getResourceAsStream("/map_ref.png")
    val backgroundImage: Image = javax.imageio.ImageIO.read(image)

    // Crea il pannello per contenere gli elementi della GUI
    // Crea il pannello per contenere gli elementi della GUI
    val panel = new JPanel(null) {
      override def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        // Disegna l'immagine di sfondo
        val widthRatio = getWidth.toDouble / backgroundImage.getWidth(null)
        val heightRatio = getHeight.toDouble / backgroundImage.getHeight(null)
        val scaleFactor = Math.max(widthRatio, heightRatio)
        val scaledWidth = (backgroundImage.getWidth(null) * scaleFactor).toInt
        val scaledHeight = (backgroundImage.getHeight(null) * scaleFactor).toInt
        val x = (getWidth - scaledWidth) / 2
        val y = (getHeight - scaledHeight) / 2
        g.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, null)
      }
    }
    panel.setPreferredSize(new Dimension(1000, 650)) // Imposta le dimensioni del pannello

    // Aggiungi gli elementi alla GUI
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


    val button2 = new JButton() {
      //setBorder(BorderFactory.createEmptyBorder())
      setContentAreaFilled(false) // Rimuove lo sfondo del bottone
      setForeground(Color.WHITE) // Imposta il colore del testo
      setFocusPainted(false) // Rimuove l'effetto di focuss
      setText(8.toString)
      setFont(new Font("Arial", 12, 10))

      override def paintComponent(g: Graphics): Unit = {
        val g2d = g.asInstanceOf[Graphics2D]
        val center = new Point2D.Float(getWidth / 2.0f, getHeight / 2.0f)
        val radius = Math.min(getWidth, getHeight) / 2.0f
        val circle = new Ellipse2D.Float(center.x - radius, center.y - radius, 2.0f * radius, 2.0f * radius)
        g2d.setColor(Color.BLUE) // Imposta il colore del cerchio
        g2d.fill(circle) // Disegna il cerchio
        super.paintComponent(g) // Disegna il testo del bottone
      }
    }
    button2.setBounds(128, 75, 40, 40)

    val button3 = new JButton() {
      //setBorder(BorderFactory.createEmptyBorder())
      setContentAreaFilled(false) // Rimuove lo sfondo del bottone
      setForeground(Color.BLACK) // Imposta il colore del testo
      setFocusPainted(false) // Rimuove l'effetto di focuss
      setText(4.toString)
      setFont(new Font("Arial", 12, 10))

      override def paintComponent(g: Graphics): Unit = {
        val g2d = g.asInstanceOf[Graphics2D]
        val center = new Point2D.Float(getWidth / 2.0f, getHeight / 2.0f)
        val radius = Math.min(getWidth, getHeight) / 2.0f
        val circle = new Ellipse2D.Float(center.x - radius, center.y - radius, 2.0f * radius, 2.0f * radius)
        g2d.setColor(Color.GREEN) // Imposta il colore del cerchio
        g2d.fill(circle) // Disegna il cerchio
        super.paintComponent(g) // Disegna il testo del bottone
      }
    }
    button3.setBounds(118, 125, 40, 40)


    panel.add(button1)
    panel.add(button2)
    panel.add(button3)

    var isSelected = false

    button1.addActionListener((_: ActionEvent) => {
      if (!isSelected) {
        button2.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2));
        button3.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2));
      }
      else {
        button2.setBorder(BorderFactory.createEmptyBorder())
        button3.setBorder(BorderFactory.createEmptyBorder())
      }
      isSelected = !isSelected
    })

    panel
}
