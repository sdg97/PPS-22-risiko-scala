package view

import controller.ControllerModule.*
import model.{PlayerColor, PlayerImpl}

import java.awt.{BorderLayout, Color, Font, Graphics, Graphics2D}
import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.geom.{Ellipse2D, Point2D}
import java.io.{File, FileReader}
import javax.swing.{BorderFactory, JButton, JFrame, JPanel}
import scala.collection.mutable
import scala.io.Source
import scala.swing.{Color, Dimension, Image}
import scala.collection.mutable.Map
import javax.swing.UIManager

object GameScreen:
  private var screen : Option[GameScreenImpl] = None
  def apply(c: Controller) =
    screen = Some(new GameScreenImpl(c))
    screen.get.screen

  def update() =
    screen.get.update()

private class GameScreenImpl(c: Controller):
  val buttonMap: mutable.Map[String, JButtonExtended] = mutable.Map()

  // Carica l'immagine di sfondo
  val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/img_map.png"))


  // Crea il pannello per contenere gli elementi della GUI
  val screen = new JPanel(null) {
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
  screen.setPreferredSize(new Dimension(1000, 650)) // Imposta le dimensioni del pannello

  val file = new File("src/main/resources/config/states.txt")
  val lines = Source.fromFile(file).getLines().toList

  lines.foreach {
    line =>
      val parts = line.split(",")
      if (parts.length >= 3) {
        val name = parts(0).trim
        val posX = parts(1).trim
        val posY = parts(2).trim

        val btnState = new JButtonExtended("", Color.YELLOW) {
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
            g2d.setColor(this.color) // Imposta il colore del cerchio
            g2d.fill(circle) // Disegna il cerchio
            super.paintComponent(g) // Disegna il testo del bottone
          }
        }

        btnState.addMouseListener(
          new MouseAdapter() {
            override def mouseEntered(evt: MouseEvent): Unit = {
              if (!btnState.isSelected && !btnState.isNeighbour)
                btnState.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
            }

            override def mouseExited(evt: MouseEvent): Unit = {
              if (!btnState.isSelected && !btnState.isNeighbour)
                btnState.setBorder(BorderFactory.createEmptyBorder())
            }
          })

        val isAttackPhase = false
        val isPositionPhase = true

        btnState.addActionListener((_: ActionEvent) => {
          if(isAttackPhase) {
            if (btnState.isNeighbour) {
              //TODO invoke attack method
              println("attack")
              resetButton()
            }
            else if (btnState.isSelected) {
              resetButton()
            }
            else {
              resetButton()
              val neighbors: Set[String] = c.getNeighbor(name, c.getCurrentPlayer())
              neighbors.foreach(neighbor => {
                val currentButton = buttonMap(neighbor)
                currentButton.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2))
                currentButton.setIsNeighbour(true)
              })
              btnState.setSelected(!btnState.isSelected)
              btnState.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
            }
          } else if(isPositionPhase) {
            c.addWagon(buttonMap.find((name, button) => {
              button.equals(btnState)
            }).get._1)
          }
        })

        btnState.setBounds(posX.toInt, posY.toInt, 40, 40)
        screen.add(btnState)
        buttonMap += (name -> btnState)
      }

      val btnShowMyStates = new JButton() {
        setText("Show my States")
      }
      btnShowMyStates.setBounds(400, 500, 200, 50)
      screen.add(btnShowMyStates)
      btnShowMyStates.addActionListener((_: ActionEvent) => {
        println(buttonMap.size)
        c.getPlayerStates(c.getCurrentPlayer()).foreach(state => buttonMap(state.name).setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2)))
      })
  }

  def resetButton(): Unit =
    buttonMap.foreach((_, button) => {
      button.setBorder(BorderFactory.createEmptyBorder())
      button.setIsNeighbour(false)
      button.setSelected(false)
    })

  def update(): Unit =
    c.getAllStates().foreach(state => {
      buttonMap(state.name).setText(state.numberOfWagon.toString)
      buttonMap(state.name).setColor(new Color(state.player.color.rgb))
    })



