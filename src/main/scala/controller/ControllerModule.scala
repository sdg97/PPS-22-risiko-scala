package controller

import model.*
import model.entity.Player
import model.entity.map.State
import model.manager.{MessageAttackPhase, SettingResult, RisikoAction, RisikoPhase, RisikoSwitchPhaseAction, VersionMap}
import view.*

/**
 * The module that represent the Controller
 */
object ControllerModule:

  /**
   * What the module provide to the other module
   */
  trait Provider:
    val controller: Controller

  /**
   * The requirements necessary for the functioning of the module
   */
  type Requirements = ViewModule.Provider with ModelModule.Provider

  /**
   * The module component used by the provider
   */
  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      def startNewGame() =
        context.view.showSettingsView()
      def setGameSettings(inputDataPlayer: List[(String, String)], typeOfMap:String) =
        context.model.setGameSettings(inputDataPlayer, typeOfMap)
      override def neighborStatesOfEnemies(stateName: String): Set[String] = model.neighborStatesOfEnemies(stateName)
      override def neighborStatesOfPlayer(state: String): Set[String] = model.neighborStatesOfPlayer(state)
      override def stateByName(stateName: String): State = model.stateByName(stateName)
      override def currentPlayerStates: Set[State] = model.currentPlayerStates
      override def currentPlayer: Player = model.currentPlayer
      override def allStates: Set[State] = model.allStates
      override def updateView(): Unit = view.update()
      override def addTank(stateName: String): Unit =
        model.addTank(stateName)
        view.update()

      override def tanksToPlace: Int = model.tanksToPlace
      override def switchTurnPhaseActionAvailable :  Set[RisikoAction] =
        model.switchTurnPhaseActionAvailable
      override def switchPhase(a: RisikoSwitchPhaseAction): Unit =
        model.switchPhase(a)
        view.update()

      override def resultOfAttack(): MessageAttackPhase = model.resultOfAttack()

      override def executeAttack(): Unit = model.executeAttack()

      override def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit =
        model.moveTanks(fromStateName, toStateName, numberOfTanks)
        view.update()

      override def rollDiceAttacker(): Seq[Int] = model.rollDiceAttacker()

      override def rollDiceDefender(): Seq[Int] = model.rollDiceDefender()

      override def numberOfTanksToMove(): Int = model.numberOfTanksToMove()

      override def setAttackerState(state: State): Unit = model.setAttackerState(state)

      override def setDefenderState(state: State): Unit = model.setDefenderState(state)

      override def setDefaultAttackSettings: Unit = model.setDefaultAttackSettings

      override def showGameView: Unit = context.view.showGameView()

      override def setDefaultInitialSettings(): Unit = model.setDefaultInitialSettings()

      override def typeOfMap(): VersionMap = model.typeOfMap()

      override def attackerState(): State = model.attackerState()

      override def defenderState(): State = model.defenderState()

      override def numberOfDiceAttacker(): Int = model.numberOfDiceAttacker()

      override def numberOfDiceDefender(): Int = model.numberOfDiceDefender()

      override def currentTurnPhase: RisikoPhase = model.currentPhase

      override def goal = model.goal

  /**
   * Module interface for mixing the module with the others MVC modules
   */
  trait Interface extends Provider with Component:
    self: Requirements =>
