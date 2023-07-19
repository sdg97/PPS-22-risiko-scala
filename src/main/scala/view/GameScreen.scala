package view

import controller.ControllerModule.*
import model.{Player, PlayerColor, PlayerImpl}
import view.component.{CurrentPlayerComponent, SelectPhaseComponent}

import java.awt.{BorderLayout, Color, Font, Graphics, Graphics2D}
import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.geom.{Ellipse2D, Point2D}
import java.io.{File, FileReader}
import javax.swing.{BorderFactory, JButton, JFrame, JLabel, JPanel, UIManager}
import scala.collection.mutable
import scala.io.Source
import scala.swing.{Color, Dimension, Image}
import scala.collection.mutable.Map

object GameScreen:
  private var screen : Option[GameScreenImpl] = None
  
  def apply(c: Controller) =
    screen = Some(new GameScreenImpl(c))
    screen.get.screen

  def update() =
    screen.get.update()

private class GameScreenImpl(c: Controller):
  private val buttonMap: mutable.Map[String, JButtonExtended] = mutable.Map()
  private val currentPlayerComponent = new CurrentPlayerComponent(c)
  private val selectPhaseComponent = new SelectPhaseComponent(c)

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

  private val file = new File("src/main/resources/config/states.txt")
  private val lines = Source.fromFile(file).getLines().toList
  private var wagonToPlace = 0

  lines.foreach {
    line =>
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
          if(color.equals(Color.BLACK) || color.equals(Color.BLUE))
            setForeground(Color.WHITE)

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

        val isAttackPhase = false
        val isPositionPhase = true
        wagonToPlace = c.wagonToPlace(c.getCurrentPlayer())

        btnState.addActionListener((_: ActionEvent) => {
          if(isPositionPhase) {
            if(c.getPlayerStates(c.getCurrentPlayer()).exists(s => s.name.equals(getStateNameFromButton(btnState))) && wagonToPlace>0)
              c.addWagon(getStateNameFromButton(btnState))
              wagonToPlace-=1
              wagonToPlaceLabel.setText("Wagon to be placed: " + wagonToPlace.toString)
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

  val turnPanel = new JPanel()
  turnPanel.add(currentPlayerComponent.get())
  turnPanel.add(selectPhaseComponent.get())
  turnPanel.setSize(turnPanel.getPreferredSize())
  screen.add(turnPanel)

  private val wagonPanel = new JPanel()
  wagonPanel.setBounds(400,0,200,40)
  private val wagonToPlaceLabel = new JLabel("Wagon to be placed: " + wagonToPlace.toString)
  wagonPanel.add(wagonToPlaceLabel)
  screen.add(wagonPanel)



  private def getStateNameFromButton(button: JButton): String =
    buttonMap.find((n, b) => {
      b.equals(button)
    }).get._1

  private def resetButton(): Unit =
    buttonMap.foreach((_, button) => {
      button.setBorder(BorderFactory.createEmptyBorder())
      button.setIsNeighbour(false)
      button.setSelected(false)
    })
  
  def update(): Unit =
    println("UPDATE LA GAME SCREEN")
    currentPlayerComponent.update()
    selectPhaseComponent.update()
    c.getAllStates().foreach(state => {
      buttonMap(state.name).setText(state.numberOfWagon.toString)
      buttonMap(state.name).setColor(new Color(state.player.color.rgb))
    })



