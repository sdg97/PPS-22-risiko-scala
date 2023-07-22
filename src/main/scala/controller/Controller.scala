package controller

import model.*

import view.*
object ControllerModule:
  trait Controller:
    def startNewGame() : Unit
    def setGameSettings(inputDataPlayer: Set[(String, String)]) : Unit
    def deployTroops() : Unit
    def getNeighbor(stateName: String, player: Player): Set[String]
    def getNeighborStatesOfPlayer(state: String, player: Player): Set[String]
    def getState(stateName: String): State
    def getPlayerStates(player: Player): Set[State]
    def getCurrentPlayer(): Player
    def getAllStates(): Set[State]
    def updateView(): Unit
    def addWagon(stateName: String): Unit
    def wagonToPlace(): Int
    def switchTurnPhaseActionAvailable :  Set[RisikoAction]
    def switchPhase(a: RisikoSwitchPhaseAction): Unit
    def rollDice(typeOfPlayer:String, state:State): Seq[Int]
    def resultAttack(attackerState: State, defenderState: State): Unit
    def attackPhase(attackerState: State, defenderState: State): Unit
    def numberOfDiceForPlayers(attackerState: State, defenderState: State):(Int,Int)
    def shiftWagon(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit
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
        context.model.getPlayers().foreach(player => println(player.username + ", " + player.color.toString))
        context.view.showGameView()


      def deployTroops() =
        model.deployTroops()
        context.view.showGameView()
        view.update()
      override def getNeighbor(stateName: String, player: Player): Set[String] = model.getNeighbor(stateName, player)
      override def getNeighborStatesOfPlayer(state: String, player: Player): Set[String] = model.getNeighborStatesOfPlayer(state, player)
      override def getState(stateName: String): State = model.getState(stateName)
      override def getPlayerStates(player: Player): Set[State] = model.getPlayerStates(player)
      override def getCurrentPlayer(): Player = model.getCurrentPlayer()
      override def getAllStates(): Set[State] = model.getAllStates
      override def updateView(): Unit = view.update()
      override def addWagon(stateName: String): Unit = model.addWagon(stateName)

      override def wagonToPlace(): Int = model.wagonToPlace()
      override def switchTurnPhaseActionAvailable :  Set[RisikoAction] =
        model.switchTurnPhaseActionAvailable
      override def switchPhase(a: RisikoSwitchPhaseAction): Unit =
        model.switchPhase(a)

      override def rollDice(typeOfPlayer: String, state: State): Seq[Int] = model.rollDice(typeOfPlayer,state)

      override def resultAttack(attackerState: State, defenderState: State): Unit = model.resultAttack(attackerState, defenderState)

      override def attackPhase(attackerState: State, defenderState: State): Unit = model.attackPhase(attackerState,defenderState)

      override def numberOfDiceForPlayers(attackerState: State, defenderState: State): (Int, Int) = model.numberOfDiceForPlayers(attackerState,defenderState)

      override def getNumberOfRollDiceAttack: Int = model.getNumberOfRollDiceAttack()

      override def shiftWagon(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit = model.shiftWagon(fromStateName, toStateName, numberOfWagon)

      override def currentTurnPhase: RisikoPhase = model.currentPhase
  trait Interface extends Provider with Component:
    self: Requirements =>
