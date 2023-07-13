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
        context.model.getSetOfPlayers().foreach(player => println(player.username + ", " + player.color.toString))
        context.view.showDeploymentTroopsView()

      def deployTroops() =
        model.deployTroops()
        context.view.showGameView()

      def getNeighbor(stateName: String, player: Player): Set[String] = model.getNeighbor(stateName, player)

      def getPlayerStates(player: Player): Set[State] = model.getPlayerStates(player)

      def getCurrentPlayer(): Player = model.getCurrentPlayer()

  trait Interface extends Provider with Component:
    self: Requirements =>
