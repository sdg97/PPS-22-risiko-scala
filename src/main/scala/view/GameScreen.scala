package view

import controller.ControllerModule.*
import model.{Player, PlayerColor, PlayerImpl, State}
import view.component.{CurrentPhaseComponent, CurrentPlayerComponent, JButtonExtended, JPanelScreen, SelectPhaseComponent, ShiftPhasePanel}

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
  private val currentPhaseComponent = new CurrentPhaseComponent(c)

  // Carica l'immagine di sfondo
  val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/img_map.png"))

  // Crea il pannello per contenere gli elementi della GUI
  val screen = new JPanelScreen(null)
  
  val panelAttackPhase = new JPanel(null) {
    setBounds(300, 80, 400, 500)
    setBackground(Color.gray)
    setBorder(BorderFactory.createLineBorder(Color.gray, 30))
    setVisible(false)
  };
  screen.add(panelAttackPhase)


  val isAttackPhase = true
  val isPositionPhase = false
  val isShiftPhase = false

  val turnPanel = new JPanel()
  turnPanel.add(currentPlayerComponent.get())
  turnPanel.add(currentPhaseComponent.get())
  turnPanel.add(selectPhaseComponent.get())
  turnPanel.setSize(turnPanel.getPreferredSize())
  screen.add(turnPanel)

  private val wagonPanel = new JPanel()
  wagonPanel.setBounds(400,0,200,40)
  private val wagonToPlaceLabel = new JLabel("Wagon to be placed: " + c.wagonToPlace().toString())
  wagonPanel.add(wagonToPlaceLabel)
  setupButtons()


  private def getStateNameFromButton(button: JButton): String =
    buttonMap.find((n, b) => {
      b.equals(button)
    }).get._1

  private def setupButtons(): Unit =
    if(isPositionPhase)
      screen.add(wagonPanel)

    c.getAllStates().foreach(state => {
      val btnState = new JButtonExtended(state.posX, state.posY)
      btnState.addActionListener((_: ActionEvent) => {
        if (isAttackPhase) {
          if (btnState.isSelected)
            resetButton()
          else if (btnState.isNeighbour) {
            println("isNeighbour")
            //se clicco su un confinante faccio l'attacco
            panelAttackPhase.removeAll()
            panelAttackPhase.revalidate()
            panelAttackPhase.repaint()
            panelAttackPhase.setVisible(true)
            val gameWindowAttack = new GameWindowAttack(panelAttackPhase, c, getStateSelected, c.getState(getStateNameFromButton(btnState)))
            gameWindowAttack.show()
            resetButton()
          } else if (!btnState.isSelected && c.getState(getStateNameFromButton(btnState)).player.equals(c.getCurrentPlayer()) && c.getState(getStateNameFromButton(btnState)).numberOfWagon>1)
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
          if(btnState.isSelected)
            resetButton()
          else if(btnState.isNeighbour)
            val shiftPanel = new ShiftPhasePanel(c, getStateSelected.name, getStateNameFromButton(btnState))
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
