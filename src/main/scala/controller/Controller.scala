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
    def switchTurnPhaseActionAvailable :  Set[RisikoAction]



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
        //context.model.setGameSettings(inputDataPlayer: Set[(String, String)])
        context.model.getPlayers().foreach(player => println(player.username + ", " + player.color.toString))
        context.view.showDeploymentTroopsView()
      def deployTroops() =
        model.deployTroops()
        context.view.showGameView()
        view.update()
      def getNeighbor(stateName: String, player: Player): Set[String] = model.getNeighbor(stateName, player)
      def getPlayerStates(player: Player): Set[State] = model.getPlayerStates(player)
      def getCurrentPlayer(): Player = model.getCurrentPlayer()
      override def getAllStates(): Set[State] = model.getAllStates
      def updateView(): Unit = view.update()
      override def addWagon(stateName: String): Unit = model.addWagon(stateName)
      override def switchTurnPhaseActionAvailable :  Set[RisikoAction] =
        model.switchTurnPhaseActionAvailable

  trait Interface extends Provider with Component:
    self: Requirements =>
