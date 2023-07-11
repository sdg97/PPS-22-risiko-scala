package view

import controller.Controller
import model.{Model, ModelImpl, PlayerColor, PlayerImpl}

import java.awt.{BorderLayout, Color, Font, Graphics, Graphics2D}
import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.geom.{Ellipse2D, Point2D}
import java.io.{File, FileReader}
import javax.swing.{BorderFactory, JButton, JFrame, JPanel}
import scala.collection.mutable
import scala.io.Source
import scala.swing.{Dimension, Image}
import scala.collection.mutable.Map
import javax.swing.UIManager
import java.awt.Color


object GameMapGui extends App {

  val model = new ModelImpl()
  val controller = new Controller(model)
  val buttonMap: mutable.Map[String, JButtonExtended] = mutable.Map()

  // Carica l'immagine di sfondo
  val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/img_map.png"))

  // Crea il frame principale
  val frame = new JFrame("GUI Map")
  frame.setLayout(new BorderLayout())
  frame.setResizable(false)

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

  val file = new File("src/main/resources/config/states.txt")
  val lines = Source.fromFile(file).getLines().toList

  lines.foreach { line =>
    val parts = line.split(",")
    if (parts.length >= 3) {
      val name = parts(0).trim
      val posX = parts(1).trim
      val posY = parts(2).trim

      val btnState = new JButtonExtended("") {
        setBorder(BorderFactory.createEmptyBorder())
        setContentAreaFilled(false) // Rimuove lo sfondo del bottone
        setForeground(Color.BLACK) // Imposta il colore del testo
        setFocusPainted(false) // Rimuove l'effetto di focuss
        setText(name)
        setFont(new Font("Arial", 12, 10))
        setRolloverEnabled(true)

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

      btnState.addMouseListener(
        new MouseAdapter() {
        override def mouseEntered(evt: MouseEvent): Unit = {
          if(!btnState.isSelected && !btnState.isNeighbour)
            btnState.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
        }

        override def mouseExited(evt: MouseEvent): Unit = {
          if(!btnState.isSelected && !btnState.isNeighbour)
            btnState.setBorder(BorderFactory.createEmptyBorder())
        }
      })

      btnState.addActionListener((_: ActionEvent) => {
        if(btnState.isNeighbour) {
          //TODO invoke attack method
          println("attack")
          resetButton()
        }
        else if(btnState.isSelected) {
          resetButton()
        }
        else {
          resetButton()
          val neighbors: Set[String] = controller.getNeighbor(name, controller.getCurrentPlayer())
          neighbors.foreach(neighbor => {
            val currentButton = buttonMap(neighbor)
            currentButton.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2))
            currentButton.setIsNeighbour(true)
          })
          btnState.setSelected(!btnState.isSelected)
          btnState.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
        }
      })

      btnState.setBounds(posX.toInt, posY.toInt, 40, 40)
      panel.add(btnState)
      buttonMap += (name -> btnState)
    }
  }

  val btnShowMyStates = new JButton() {
    setText("Show my States")
  }
  btnShowMyStates.setBounds(400,500,200,50)
  panel.add(btnShowMyStates)
  btnShowMyStates.addActionListener((_: ActionEvent) => {
    println(buttonMap.size)
    controller.getPlayerStates(controller.getCurrentPlayer()).foreach(state => buttonMap(state.name).setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2)))
  })

  // Aggiungi il pannello al frame
  frame.getContentPane.add(panel)
  // Imposta le dimensioni del frame e visualizzalo
  frame.pack()
  frame.setVisible(true)

  def resetButton(): Unit =
    buttonMap.foreach((_, button) => {
      button.setBorder(BorderFactory.createEmptyBorder())
      button.setIsNeighbour(false)
      button.setSelected(false)
    })
}
