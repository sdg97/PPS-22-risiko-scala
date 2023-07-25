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
    def players: Set[Player]
    def deployTroops(): Unit
    def neighbors(stateName: String, player: Player): Set[String]
    def neighborStatesOfPlayer(state: String, player: Player): Set[String]
    def playerStates(player: Player): Set[State]
    def allStates: Set[State]
    def attack(attacker: State, defender: State): Unit
    @throws(classOf[MyCustomException])
    def attackResult(attackerState: State, defenderState: State): Unit
    def rollDice(typeOfPlayer:String, state:State): Seq[Int]
    def numberOfDiceForPlayers(attackerState: State, defenderState: State):(Int,Int)
    def numberOfRollDiceAttack():Int
    def currentPlayer: Player
    def stateByName(stateName: String): State
    def addTank(stateName: String): Unit
    def tanksToPlace: Int
    def switchTurnPhaseActionAvailable : Set[RisikoAction]
    def switchPhase(a: RisikoSwitchPhaseAction): Unit
    def currentPhase: RisikoPhase
    def moveTanks(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit
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
      SetupFromFiles.setup(gameMap)

      override def neighbors(stateName: String, player: Player): Set[String] = gameMap.neighborStates(stateName, player)
      override def neighborStatesOfPlayer(state: String, player: Player): Set[String] = gameMap.neighborStatesOfPlayer(state, player)

      override def playerStates(player: Player): Set[State] =
        gameMap.playerStates(player)

      override def currentPlayer: Player = turnManager.get.current

      override def stateByName(stateName: String): State = gameMap.stateByName(stateName)

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
            Player(element._1, PlayerColor.valueOf(element._2))
          )))
          turnManager.get.next()
          gameMap.assignStatesToPlayers(turnManager.get.all)
          gameMap.calcWagonToPlace(currentPlayer)
        }
      }

      override def deployTroops(): Unit = println("troop deployed")

      override def players: Set[Player] = turnManager.get.all

      override def allStates: Set[State] = gameMap.nodes


      override def addTank(stateName: String): Unit =
        if(currentPlayer.equals(gameMap.stateByName(stateName).player) && currentPlayer.tanksToPlace > 0)
          gameMap.stateByName(stateName).addWagon(1)
          currentPlayer.setTanksToPlace(currentPlayer.tanksToPlace-1)

      override def tanksToPlace: Int = currentPlayer.tanksToPlace
      override def switchTurnPhaseActionAvailable :  Set[RisikoAction] = turnPhasesManager.permittedAction

      override def switchPhase(a: RisikoSwitchPhaseAction): Unit = a match
        case EndTurn => turnPhasesManager.trigger(a); turnManager.get.next(); gameMap.calcWagonToPlace(currentPlayer)
        case _ => turnPhasesManager.trigger(a)

      override def attack(attacker: State, defender: State): Unit =
        var wagonlostAttacker: Int = 0;
        var wagonlostDefender: Int = 0;
        rollDiceAttack.zip(rollDiceDefender).map { case (elem1, elem2) =>
          if (elem1 > elem2)
            wagonlostDefender = wagonlostDefender + 1
          else if (elem1 <= elem2)
            wagonlostAttacker = wagonlostAttacker + 1
        }
        attacker.removeWagon(wagonlostAttacker)
        defender.removeWagon(wagonlostDefender)

      override def attackResult(attackerState: State, defenderState: State): Unit = {
        if (attackerState.numberOfWagon > 1 && defenderState.numberOfWagon == 0) {
          defenderState.setPlayer(attackerState.player)
          if(checkWinner())
            throw new MyCustomException("""<html>Great, you are the Winner <br>""")
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
            numberOfDice = 3
          } else {
            numberOfDice = state.numberOfWagon - 1
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

      override def numberOfRollDiceAttack(): Int = rollDiceAttack.size

      override def moveTanks(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit =
        gameMap.shiftWagon(fromStateName, toStateName, numberOfWagon)

      private def checkWinner(): Boolean =
        playerStates(currentPlayer).size >= 24

      override def currentPhase: RisikoPhase =
        turnPhasesManager.currentPhase

  trait Interface extends Provider with Component:
    self: Requirements =>

