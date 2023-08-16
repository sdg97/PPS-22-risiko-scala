package controller

import model.*
import model.entity.Player
import model.entity.map.State
import model.manager.{MessageAttackPhase, SettingResult, RisikoAction, RisikoPhase, RisikoSwitchPhaseAction, VersionMap}
import view.*
object ControllerModule:
  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

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

      override def resultAttack(): MessageAttackPhase = model.attackResult()

      override def attackPhase(): Unit = model.attack()

      override def numberOfDiceForPlayers(attacker: State, defender: State): (Int, Int) = model.numberOfDiceForPlayers(attacker, defender)

      override def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit =
        model.moveTanks(fromStateName, toStateName, numberOfTanks)
        view.update()

      override def rollDiceAttacker(): Seq[Int] = model.rollDiceAttacker()

      override def rollDiceDefender(): Seq[Int] = model.rollDiceDefender()

      override def numberOfTanksToMove(attacker: State): Int = model.numberOfTanksToMove(attacker)

      override def setAttacker(state: State): Unit = model.setAttacker(state)

      override def setDefender(state: State): Unit = model.setDefender(state)

      override def setDefaultAttackSettings: Unit = model.setDefaultAttackSettings

      override def showGameView: Unit = context.view.showGameView()

      override def setDefaultInitialSettings(): Unit = model.setDefaultInitialSettings()

      override def setTypeOfMap(): VersionMap = model.setTypeOfMap()

      override def getAttacker(): State = model.getAttacker()

      override def getDefender(): State = model.getDefender()

      override def currentTurnPhase: RisikoPhase = model.currentPhase

      override def goal = model.goal
  trait Interface extends Provider with Component:
    self: Requirements =>
