package view

import controller.ControllerModule.*
import model.{Player, PlayerColor, PlayerImpl, State}
import view.component.{CurrentPlayerComponent, SelectPhaseComponent}

import java.awt.{BasicStroke, BorderLayout, Color, FlowLayout, Font, Graphics, Graphics2D, Polygon}
import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.geom.{Ellipse2D, Point2D}
import java.io.{File, FileReader}
import java.util.Random
import javax.swing.{BorderFactory, JButton, JComponent, JFrame, JLabel, JPanel, SwingConstants, UIManager}
import scala.collection.mutable
import scala.io.Source
import scala.swing.{Color, Dimension, Image}
import scala.collection.mutable.Map
import scala.swing.MenuBar.NoMenuBar.name


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

  val panelAttackPhase = new JPanel(null) {
    setBounds(300, 80, 400, 500)
    setBackground(Color.gray)
    setBorder(BorderFactory.createLineBorder(Color.gray, 30))
    setVisible(false)
  };
  screen.add(panelAttackPhase)


  val isAttackPhase = false
  val isPositionPhase = false
  val isShiftPhase = true

  val turnPanel = new JPanel()
  turnPanel.add(currentPlayerComponent.get())
  turnPanel.add(selectPhaseComponent.get())
  turnPanel.setSize(turnPanel.getPreferredSize())
  screen.add(turnPanel)

  private val wagonPanel = new JPanel()
  wagonPanel.setBounds(400,0,200,40)
  private val wagonToPlaceLabel = new JLabel("Wagon to be placed: " + c.wagonToPlace().toString())
  wagonPanel.add(wagonToPlaceLabel)
  screen.add(wagonPanel)
  setupButtons()

  private def getStateNameFromButton(button: JButton): String =
    buttonMap.find((n, b) => {
      b.equals(button)
    }).get._1

  private def setupButtons(): Unit =
    c.getAllStates().foreach(state => {
      val btnState = new JButtonExtended("") {
        setBorder(BorderFactory.createEmptyBorder())
        setContentAreaFilled(false) // Rimuove lo sfondo del bottone
        setForeground(Color.BLACK) // Imposta il colore del testo
        setFocusPainted(false) // Rimuove l'effetto di focuss
        setFont(new Font("Arial", 12, 10))
        setRolloverEnabled(true)
        if (color.equals(Color.BLACK) || color.equals(Color.BLUE))
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

      btnState.addActionListener((_: ActionEvent) => {
        if (isAttackPhase) {
          if (btnState.isNeighbour) {
            println("isNeighbour")
            //se clicco su un confinante faccio l'attacco
            panelAttackPhase.removeAll()
            panelAttackPhase.setVisible(true)
            val gameWindowAttack = new GameWindowAttack(panelAttackPhase, this, c, getStateSelected, c.getState(getStateNameFromButton(btnState)))
            gameWindowAttack.show()
            resetButton()
          } else if (!btnState.isSelected && c.getState(getStateNameFromButton(btnState)).player.equals(c.getCurrentPlayer()))
            resetButton()
            println("isSelected")

            //se clicco su un bottone che non è selezionato lo setto come selezionato
            btnState.setSelected(true)
            //setto tutti i confinanti degli altri giocatori come confinanti
            c.getNeighbor(getStateNameFromButton(btnState), c.getCurrentPlayer()).foreach(stateName => {
              buttonMap(stateName).setIsNeighbour(true)
              buttonMap(stateName).setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2))
            })
        }
        else if (isPositionPhase) {
          resetButton()
          c.addWagon(getStateNameFromButton(btnState))
          wagonToPlaceLabel.setText("Wagon to be placed: " + c.wagonToPlace().toString)
        }
        else if (isShiftPhase) {
          if(btnState.isNeighbour)
            c.shiftWagon(getStateSelected.name, getStateNameFromButton(btnState), 1)
            resetButton()
          else if(!btnState.isSelected && c.getState(getStateNameFromButton(btnState)).player.equals(c.getCurrentPlayer()))
            resetButton()
            btnState.setSelected(true)
            c.getNeighborStatesOfPlayer(getStateNameFromButton(btnState),c.getCurrentPlayer()).foreach(stateName => {
              buttonMap(stateName).setIsNeighbour(true)
              buttonMap(stateName).setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
            })
        }
      })
      btnState.setBounds(state.posX, state.posY, 40, 40)
      screen.add(btnState)
      buttonMap += (state.name -> btnState)
      buttonMap(state.name).setText(state.numberOfWagon.toString)
      buttonMap(state.name).setColor(new Color(state.player.color.rgb))
    })


  private def resetButton(): Unit =
    buttonMap.foreach((_, button) => {
      button.setBorder(BorderFactory.createEmptyBorder())
      button.setIsNeighbour(false)
      button.setSelected(false)
    })

  private def getStateSelected: State =
    c.getState(buttonMap.find((_, button) => button.isSelected).get._1)


  def update(): Unit =
    println("UPDATE LA GAME SCREEN")

    currentPlayerComponent.update()
    selectPhaseComponent.update()
    c.getAllStates().foreach(state => {
      buttonMap(state.name).setText(state.numberOfWagon.toString)
      buttonMap(state.name).setColor(new Color(state.player.color.rgb))
    })
