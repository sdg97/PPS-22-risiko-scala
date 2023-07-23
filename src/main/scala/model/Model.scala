package model
import controller.ControllerModule
import view.ViewModule.Requirements

import scala.util.Random
import controller.ControllerModule
import utils.SetupFromFiles
import view.ViewModule.Requirements

object ModelModule:
  trait Model {

    @throws(classOf[MyCustomException])
    def setGameSettings(inputDataPlayer: Set[(String, String)]): Unit
    def getPlayers(): Set[Player]
    def deployTroops(): Unit
    def getNeighbor(stateName: String, player: Player): Set[String]
    def getNeighborStatesOfPlayer(state: String, player: Player): Set[String]
    def getPlayerStates(player: Player): Set[State]
    def getAllStates: Set[State]
    def resultAttack(attackerState: State, defenderState: State): Unit
    @throws(classOf[MyCustomException])
    def attackPhase(attackerState: State, defenderState: State): Unit
    def rollDice(typeOfPlayer:String, state:State): Seq[Int]
    def numberOfDiceForPlayers(attackerState: State, defenderState: State):(Int,Int)
    def getCurrentPlayer(): Player
    def getState(stateName: String): State
    def updateView(): Unit
    def addWagon(stateName: String): Unit
    def wagonToPlace(): Int
    def switchTurnPhaseActionAvailable : Set[RisikoAction]
    def switchPhase(a: RisikoSwitchPhaseAction): Unit
    def currentPhase: RisikoPhase
    def shiftWagon(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit
    def getNumberOfRollDiceAttack():Int

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

      SetupFromFiles.setup(gameMap)

      override def getNeighbor(stateName: String, player: Player): Set[String] = gameMap.getNeighborStates(stateName, player)
      override def getNeighborStatesOfPlayer(state: String, player: Player): Set[String] = gameMap.getNeighborStatesOfPlayer(state, player)

      override def getPlayerStates(player: Player): Set[State] =
        gameMap.getPlayerStates(player)

      override def getCurrentPlayer(): Player = turnManager.get.current()

      override def getState(stateName: String): State = gameMap.getStateByName(stateName)

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


      override def resultAttack(attackerState: State, defenderState: State): Unit =
        var wagonlostAttacker: Int = 0;
        var wagonlostDefender: Int = 0;
        rollDiceAttack.zip(rollDiceDefender).map { case (elem1, elem2) =>
          if (elem1 > elem2)
            wagonlostDefender = wagonlostDefender + 1
          else if (elem1 <= elem2)
            wagonlostAttacker = wagonlostAttacker + 1
        }
        attackerState.removeWagon(wagonlostAttacker)
        defenderState.removeWagon(wagonlostDefender)

      override def attackPhase(attackerState: State, defenderState: State): Unit = {
        if (attackerState.numberOfWagon > 1 && defenderState.numberOfWagon == 0) {
          defenderState.setPlayer(attackerState.player)
          throw new MyCustomException("""<html>Great, you conquered <br>""" + defenderState.name)
        }
        else if (attackerState.numberOfWagon == 1) {
          throw new MyCustomException("""<html>Sorry, but you can't attack <br>because you have only one wagon <br> in """ + defenderState.name + """</html>""".stripMargin)
        }
      }

      private var rollDiceAttack = Seq[Int]()
      private var rollDiceDefender = Seq[Int]()

      override def rollDice(typeOfPlayer: String, state: State): Seq[Int] = {
        var numberOfDice: Int = 0;
        var resultRollDice = Seq[Int]()
        if (typeOfPlayer.equals("attack")) {
          if (state.numberOfWagon > 3) {
            numberOfDice = 3;
          } else {
            numberOfDice = state.numberOfWagon - 1;
          }
          resultRollDice = Seq.fill(numberOfDice)(Random.nextInt(6) + 1).sorted.reverse
          rollDiceAttack = resultRollDice
        }
        else {
          if (state.numberOfWagon >= 3) {
            numberOfDice = 3;
          } else {
            numberOfDice = state.numberOfWagon;
          }
          resultRollDice = Seq.fill(numberOfDice)(Random.nextInt(6) + 1).sorted.reverse
          rollDiceDefender = resultRollDice
        }
        resultRollDice
      }

      override def numberOfDiceForPlayers(attackerState: State, defenderState: State): (Int, Int) = {
        var numberOfDiceAttack = 0
        var numberOfDiceDefender = 0
        if (attackerState.numberOfWagon > 3) {
          numberOfDiceAttack = 3;
        } else {
          numberOfDiceAttack = attackerState.numberOfWagon - 1;
        }

        if (defenderState.numberOfWagon >= 3) {
          numberOfDiceDefender = 3;
        } else {
          numberOfDiceDefender = defenderState.numberOfWagon;
        }
        (numberOfDiceAttack, numberOfDiceDefender)
      }

      override def getNumberOfRollDiceAttack(): Int = rollDiceAttack.size

      override def shiftWagon(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit =
        gameMap.shiftWagon(fromStateName, toStateName, numberOfWagon)
        controller.updateView()

      override def currentPhase: RisikoPhase =
        turnPhasesManager.currentPhase

  trait Interface extends Provider with Component:
    self: Requirements =>

