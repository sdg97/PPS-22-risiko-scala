package view.screen

import controller.Controller
import model.entity.map.State
import model.entity.{Player, PlayerColor}
import model.manager.RisikoPhase
import view.screen.GameScreen
import view.component.*
import view.window.{GameWindowAttack, MoveWindow}

import java.awt.*
import java.awt.event.{ActionEvent, ActionListener, MouseAdapter, MouseEvent, MouseListener}
import java.awt.geom.{Ellipse2D, Point2D}
import java.io.{File, FileReader}
import java.util.Random
import javax.swing.*
import scala.collection.mutable
import scala.collection.mutable.Map
import scala.io.Source
import scala.swing.MenuBar.NoMenuBar.name
import scala.swing.{Color, Container, Dimension, Image}

trait GameScreen:
  def setClickable(enable:Boolean): Unit

object GameScreen:
  private var screen : Option[GameScreenImpl] = None
  def apply(c: Controller) =
    screen = Some(new GameScreenImpl(c))
    screen.get.screen

  def update() =
    screen.get.update()

private class GameScreenImpl(controller: Controller) extends GameScreen:
  private val buttonMap: mutable.Map[String, JButtonExtended] = mutable.Map()
  private val currentPlayerComponent = new CurrentPlayerComponent(controller)
  private val selectPhaseComponent = new SelectPhaseComponent(controller)
  private val currentPhaseComponent = new CurrentPhaseComponent(controller)
  private val goalComponent = new GoalComponent(controller)

  val screen = new Background(null, controller.setTypeOfMap())

  private val turnPanel = new JPanel()
  turnPanel.add(currentPlayerComponent.get())
  turnPanel.add(currentPhaseComponent.get())
  turnPanel.add(selectPhaseComponent.get())
  turnPanel.add(goalComponent.get())
  turnPanel.setBounds(0,0,800,  40)
  screen.add(turnPanel)

  private val tanksPanel = new JPanel()
  tanksPanel.setBounds(800,0,200,40)
  private val tanksToPlaceLabel = new JLabel(s"Tanks to be placed: ${controller.tanksToPlace}")

  tanksPanel.add(tanksToPlaceLabel)
  setupButtons()

  private def getStateNameFromButton(button: JButton): String =
    buttonMap.find((_, b) => {
      b.equals(button)
    }).get._1

  private def setupButtons(): Unit =
    if(controller.currentTurnPhase.equals(RisikoPhase.StartTurn))
      screen.add(tanksPanel)
    controller.allStates.foreach( state => {
      val btnState = new JButtonExtended(state.position)
      btnState.addActionListener(createButtonActionListener(btnState))
      btnState.addMouseListener(createButtonMouseListener(btnState))
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
    if (controller.currentTurnPhase.equals(RisikoPhase.StartTurn))
      tanksToPlaceLabel.setText(s"Tanks to be placed: ${controller.tanksToPlace}")
      tanksPanel.setVisible(true)
    else
      tanksPanel.setVisible(false)

  private def getStateSelected: State =
    controller.stateByName(buttonMap.find((_, button) => button.isSelected).get._1)

  def update(): Unit =
    if(buttonMap.head._2.isEnabled)
      selectPhaseComponent.update()
    currentPhaseComponent.update()
    currentPlayerComponent.update()
    controller.allStates.foreach(state => {
      buttonMap(state.name).setText(state.numberOfTanks.toString)
      buttonMap(state.name).setColor(new Color(state.player.color.rgb))
    })
    resetButton()

  def setClickable(enable:Boolean): Unit =
    buttonMap.values.foreach(_.setEnabled(enable))
    selectPhaseComponent.setButtonsEnabled(enable)
    if(enable)
      selectPhaseComponent.update()

  private def createButtonActionListener(btnState: JButtonExtended): ActionListener = (e: ActionEvent) => {
    controller.currentTurnPhase match {
      case RisikoPhase.StartTurn =>
        resetButton()
        controller.addTank(getStateNameFromButton(btnState))
        tanksToPlaceLabel.setText(s"Tanks to be placed: ${controller.tanksToPlace}")

      case RisikoPhase.Attack =>
        if (btnState.isSelected)
          resetButton()
        else if (btnState.isNeighbour) {
          println("isNeighbour")
          //if i click on a neighbor state I do an attack
          controller.setAttacker(getStateSelected)
          controller.setDefender(controller.stateByName(getStateNameFromButton(btnState)))
          val gameWindowAttack = new GameWindowAttack(this, controller)
          resetButton()
        } else if (!btnState.isSelected && controller.stateByName(getStateNameFromButton(btnState)).player.equals(controller.currentPlayer) && controller.stateByName(getStateNameFromButton(btnState)).numberOfTanks > 1) {
          resetButton()
          println("isSelected")
          //set button as selected
          btnState.setSelected(true)
          //set neighbor states
          controller.neighborStatesOfEnemies(getStateNameFromButton(btnState)).foreach(stateName => {
            buttonMap(stateName).setIsNeighbour(true)
            buttonMap(stateName).setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2))
          })
        }
      case RisikoPhase.Move =>
        if (btnState.isSelected)
          resetButton()
        else if (btnState.isNeighbour) {
          val movePanel = new MoveWindow(this, controller, getStateSelected.name, getStateNameFromButton(btnState))
          resetButton()
        } else if (!btnState.isSelected && controller.stateByName(getStateNameFromButton(btnState)).player.equals(controller.currentPlayer)) {
          resetButton()
          //set button as selected
          btnState.setSelected(true)
          //set neighbor states
          controller.neighborStatesOfPlayer(getStateNameFromButton(btnState)).foreach(stateName => {
            buttonMap(stateName).setIsNeighbour(true)
            buttonMap(stateName).setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
          })
        }
    }
  }

  private def createButtonMouseListener(btnState: JButtonExtended): MouseListener = new MouseAdapter {
    override def mouseEntered(evt: MouseEvent): Unit = {
      if (!btnState.isSelected && !btnState.isNeighbour && controller.stateByName(getStateNameFromButton(btnState)).player.equals(controller.currentPlayer)) {
        btnState.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
      }
    }
    override def mouseExited(evt: MouseEvent): Unit = {
      if (!btnState.isSelected && !btnState.isNeighbour && controller.stateByName(getStateNameFromButton(btnState)).player.equals(controller.currentPlayer)) {
        btnState.setBorder(BorderFactory.createEmptyBorder())
      }
    }
  }

