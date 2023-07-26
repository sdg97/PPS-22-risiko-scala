package controller

import model.*

import view.*
object ControllerModule:
  trait Controller:
    def startNewGame() : Unit
    def setGameSettings(inputDataPlayer: Set[(String, String)]) : Unit
    def deployTroops() : Unit
    def neighborStatesOfEnemies(stateName: String): Set[String]
    def neighborStatesOfPlayer(state: String): Set[String]
    def stateByName(stateName: String): State
    def currentPlayerStates: Set[State]
    def currentPlayer: Player
    def allStates: Set[State]
    def updateView(): Unit
    def addTank(stateName: String): Unit
    def tanksToPlace: Int
    def switchTurnPhaseActionAvailable :  Set[RisikoAction]
    def switchPhase(a: RisikoSwitchPhaseAction): Unit
    def rollDice(typeOfPlayer:String, state:State): Seq[Int]
    def resultAttack(attackerState: State, defenderState: State): Unit
    def attackPhase(attackerState: State, defenderState: State): Unit
    def numberOfDiceForPlayers(attackerState: State, defenderState: State):(Int,Int)
    def moveTanks(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit
    def getNumberOfRollDiceAttack:Int
    def currentTurnPhase: RisikoPhase

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      def startNewGame() =
        context.view.showSettingsView()
      def setGameSettings(inputDataPlayer: Set[(String, String)]) =
        context.model.setGameSettings(inputDataPlayer)
        context.model.players.foreach(player => println(player.username + ", " + player.color.toString))
        context.view.showGameView()


      def deployTroops() =
        model.deployTroops()
        context.view.showGameView()
        view.update()
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

      override def rollDice(typeOfPlayer: String, state: State): Seq[Int] = model.rollDice(typeOfPlayer,state)

      override def resultAttack(attackerState: State, defenderState: State): Unit = model.attack(attackerState, defenderState)

      override def attackPhase(attackerState: State, defenderState: State): Unit = model.attackResult(attackerState,defenderState)

      override def numberOfDiceForPlayers(attackerState: State, defenderState: State): (Int, Int) = model.numberOfDiceForPlayers(attackerState,defenderState)

      override def getNumberOfRollDiceAttack: Int = model.numberOfRollDiceAttack()

      override def moveTanks(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit =
        model.moveTanks(fromStateName, toStateName, numberOfWagon)
        view.update()

      override def currentTurnPhase: RisikoPhase = model.currentPhase
  trait Interface extends Provider with Component:
    self: Requirements =>
