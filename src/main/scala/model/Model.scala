package model

import controller.ControllerModule
import view.ViewModule.Requirements

object ModelModule:
  trait Model {

    @throws(classOf[MyCustomException])
    def setGameSettings(inputDataPlayer: Set[(String, String)]): Unit

    def getPlayers(): Set[Player]
    def deployTroops(): Unit
    def getNeighbor(stateName: String, player: Player): Set[String]
    def getPlayerStates(player: Player): Set[State]
    def getAllStates: Set[State]
    def getCurrentPlayer(): Player
    def updateView(): Unit
    def addWagon(stateName: String): Unit
    def wagonToPlace(): Int
    def switchTurnPhaseActionAvailable : Set[RisikoAction]
    def switchPhase(a: RisikoSwitchPhaseAction): Unit
  }

  type Requirements = ControllerModule.Provider

  trait Provider:
    val model: Model

  trait Component:
    context: Requirements =>

    import java.io.File
    import scala.io.Source
    import RisikoSwitchPhaseAction.*

    class ModelImpl extends Model:
      private val gameMap = new GameMap()
      private var turnManager : Option[TurnManager[Player]] = None
      private val turnPhasesManager = TurnPhasesManager()
      private val stateFile = new File("src/main/resources/config/states.txt")
      private val stateFileLines: Seq[String] = Source.fromFile(stateFile).getLines().toList

      stateFileLines.foreach { line =>
        val parts = line.split(",")
        if (parts.length >= 3) {
          val name = parts(0).trim
          val posX = parts(1).trim
          val posY = parts(2).trim
          gameMap.addNode(new StateImpl(name, 0, null, posX.toInt, posY.toInt))
        }
      }

      val borderFile = new File("src/main/resources/config/borders.txt")
      val borderFileLines: Seq[String] = Source.fromFile(borderFile).getLines().toList
      borderFileLines.foreach { line =>
        val parts = line.split(",")
        if (parts.length >= 2) {
          val state1 = parts(0).trim
          val state2 = parts(1).trim
          gameMap.addEdge(state1, state2)
        }
      }

      override def getNeighbor(stateName: String, player: Player): Set[String] = gameMap.getNeighborStates(stateName, player)

      override def getPlayerStates(player: Player): Set[State] =
        gameMap.getPlayerStates(player)

      override def getCurrentPlayer(): Player = turnManager.get.current()

      override def setGameSettings(inputDataPlayer: Set[(String, String)]): Unit = {
        if (inputDataPlayer.exists(_._1 == "")) {
          throw new MyCustomException("All username field must be completed")
        }
        else if (inputDataPlayer.exists(element => inputDataPlayer.count(_._2 == element._2) > 1)) {
          throw new MyCustomException("A color must be assigned at only one player")
        }
        else if (inputDataPlayer.exists(element => inputDataPlayer.count(_._1 == element._1) > 1)) {
          throw new MyCustomException("A username must be assigned at only one player")
        }
        else {
          turnManager = Some(TurnManager(inputDataPlayer.map(element =>
            new PlayerImpl(element._1, PlayerColor.valueOf(element._2))
          )))
          turnManager.get.next()
          gameMap.assignStatesToPlayers(turnManager.get.getAll())
          gameMap.calcWagonToPlace(getCurrentPlayer())
        }
      }

      override def deployTroops(): Unit = println("troop deployed")

      override def getPlayers(): Set[Player] = turnManager.get.getAll()

      override def getAllStates: Set[State] = gameMap.nodes

      override def updateView(): Unit = controller.updateView()

      override def addWagon(stateName: String): Unit =
        val currentPlayer = getCurrentPlayer()
        if(currentPlayer.equals(gameMap.getStateByName(stateName).player) && currentPlayer.wagonToPlace > 0)
          gameMap.getStateByName(stateName).addWagon(1)
          currentPlayer.setWagonToPlace(currentPlayer.wagonToPlace-1)
          controller.updateView()

      override def wagonToPlace(): Int = getCurrentPlayer().wagonToPlace
      override def switchTurnPhaseActionAvailable :  Set[RisikoAction] = turnPhasesManager.permittedAction

      override def switchPhase(a: RisikoSwitchPhaseAction): Unit = a match
        case EndTurn => turnPhasesManager.trigger(a); turnManager.get.next(); gameMap.calcWagonToPlace(getCurrentPlayer())
        case _ => turnPhasesManager.trigger(a)

  trait Interface extends Provider with Component:
    self: Requirements =>


