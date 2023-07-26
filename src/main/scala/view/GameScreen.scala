package view

import controller.ControllerModule.*
import model.{Player, PlayerColor, RisikoPhase, State}
import view.component.{CurrentPhaseComponent, CurrentPlayerComponent, JButtonExtended, JPanelScreen, SelectPhaseComponent, MovePhasePanel}

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

  val turnPanel = new JPanel()
  turnPanel.add(currentPlayerComponent.get())
  turnPanel.add(currentPhaseComponent.get())
  turnPanel.add(selectPhaseComponent.get())
  turnPanel.setBounds(0,0,700,  40)
  screen.add(turnPanel)

  private val wagonPanel = new JPanel()
  wagonPanel.setBounds(800,0,200,40)
  private val wagonToPlaceLabel = new JLabel(s"Wagon to be placed: ${c.tanksToPlace}")

  wagonPanel.add(wagonToPlaceLabel)
  setupButtons()

  private def getStateNameFromButton(button: JButton): String =
    buttonMap.find((n, b) => {
      b.equals(button)
    }).get._1

  private def setupButtons(): Unit =
    if(c.currentTurnPhase.equals(RisikoPhase.StartTurn))
      screen.add(wagonPanel)
    c.allStates.foreach(state => {
      val btnState = new JButtonExtended(state.posX, state.posY)
      btnState.addActionListener((_: ActionEvent) => {
        c.currentTurnPhase match {
          case RisikoPhase.StartTurn =>
            resetButton()
            c.addTank(getStateNameFromButton(btnState))
            wagonToPlaceLabel.setText("Wagon to be placed: " + c.tanksToPlace.toString)
          case RisikoPhase.Attack =>
            if (btnState.isSelected)
              resetButton()
            else if (btnState.isNeighbour) {
              println("isNeighbour")
              //se clicco su un confinante faccio l'attacco
              val gameWindowAttack = new GameWindowAttack(c, getStateSelected, c.stateByName(getStateNameFromButton(btnState)))
              resetButton()
            } else if (!btnState.isSelected && c.stateByName(getStateNameFromButton(btnState)).player.equals(c.currentPlayer) && c.stateByName(getStateNameFromButton(btnState)).numberOfTanks>1)
              resetButton()
              println("isSelected")
              //se clicco su un bottone che non è selezionato lo setto come selezionato
              btnState.setSelected(true)
              //setto tutti i confinanti degli altri giocatori come confinanti
              c.neighborStatesOfEnemies(getStateNameFromButton(btnState)).foreach(stateName => {
                buttonMap(stateName).setIsNeighbour(true)
                buttonMap(stateName).setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2))
              })

          case RisikoPhase.Move =>
            if(btnState.isSelected)
              resetButton()
            else if(btnState.isNeighbour)
              val movePanel = new MovePhasePanel(c, getStateSelected.name, getStateNameFromButton(btnState))
              resetButton()
            else if(!btnState.isSelected && c.stateByName(getStateNameFromButton(btnState)).player.equals(c.currentPlayer))
              resetButton()
              btnState.setSelected(true)
              c.neighborStatesOfPlayer(getStateNameFromButton(btnState)).foreach(stateName => {
                buttonMap(stateName).setIsNeighbour(true)
                buttonMap(stateName).setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
              })
        }
      })

      btnState.addMouseListener(new MouseAdapter() {
        override def mouseEntered(evt: MouseEvent): Unit = {
          if (!btnState.isSelected && !btnState.isNeighbour && c.stateByName(getStateNameFromButton(btnState)).player.equals(c.currentPlayer))
            btnState.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
        }
        override def mouseExited(evt: MouseEvent): Unit = {
          if (!btnState.isSelected && !btnState.isNeighbour && c.stateByName(getStateNameFromButton(btnState)).player.equals(c.currentPlayer))
            btnState.setBorder(BorderFactory.createEmptyBorder())
        }
      })

      screen.add(btnState)
      buttonMap += (state.name -> btnState)
      buttonMap(state.name).setText(state.numberOfTanks.toString)
      buttonMap(state.name).setColor(new Color(state.player.color.rgb))
    })

  private def resetButton(): Unit =
    buttonMap.foreach((_, button) => {
      button.setBorder(BorderFactory.createEmptyBorder())
      button.setIsNeighbour(false)
      button.setSelected(false)
    })
    if (c.currentTurnPhase.equals(RisikoPhase.StartTurn))
      wagonToPlaceLabel.setText(s"Wagon to be placed: ${c.tanksToPlace}")
      wagonPanel.setVisible(true)
    else
      wagonPanel.setVisible(false)

  private def getStateSelected: State =
    c.stateByName(buttonMap.find((_, button) => button.isSelected).get._1)

  def update(): Unit =
    currentPhaseComponent.update()
    currentPlayerComponent.update()
    selectPhaseComponent.update()
    c.allStates.foreach(state => {
      buttonMap(state.name).setText(state.numberOfTanks.toString)
      buttonMap(state.name).setColor(new Color(state.player.color.rgb))
    })
    resetButton()