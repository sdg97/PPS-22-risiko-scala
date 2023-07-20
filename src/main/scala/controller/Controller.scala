package controller

import model.*
import view.*
object ControllerModule:
  trait Controller:
    def startNewGame() : Unit
    def setGameSettings(inputDataPlayer: Set[(String, String)]) : Unit
    def deployTroops() : Unit
    def getNeighbor(stateName: String, player: Player): Set[String]
    def getPlayerStates(player: Player): Set[State]
    def getCurrentPlayer(): Player

    def getAllStates(): Set[State]

    def updateView(): Unit
    def addWagon(stateName: String): Unit
    def rollDice(typeOfPlayer:String, state:State): Seq[Int]
    def resultAttack(attackerState: State, defenderState: State): Unit
    def attackPhase(attackerState: State, defenderState: State): Unit
    def numberOfDiceForPlayers(attackerState: State, defenderState: State):(Int,Int)

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
        context.model.getSetOfPlayers().foreach(player => println(player.username + ", " + player.color.toString))
        context.view.showGameView()

      def deployTroops() =
        model.deployTroops()
        context.view.showGameView()

      def getNeighbor(stateName: String, player: Player): Set[String] = model.getNeighbor(stateName, player)

      def getPlayerStates(player: Player): Set[State] = model.getPlayerStates(player)

      def getCurrentPlayer(): Player = model.getCurrentPlayer()

      override def getAllStates(): Set[State] = model.getAllStates

      def updateView(): Unit = view.update()

      override def rollDice(typeOfPlayer: String, state: State): Seq[Int] = model.rollDice(typeOfPlayer,state)

      override def resultAttack(attackerState: State, defenderState: State): Unit = model.resultAttack(attackerState, defenderState)

      override def attackPhase(attackerState: State, defenderState: State): Unit = model.attackPhase(attackerState,defenderState)

      override def numberOfDiceForPlayers(attackerState: State, defenderState: State): (Int, Int) = model.numberOfDiceForPlayers(attackerState,defenderState)

      override def addWagon(stateName: String): Unit = model.addWagon(stateName)

  trait Interface extends Provider with Component:
    self: Requirements =>
