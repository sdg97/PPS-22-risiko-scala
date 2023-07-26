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
    def neighborStatesOfEnemies(stateName: String): Set[String]
    def neighborStatesOfPlayer(state: String): Set[String]
    def currentPlayerStates: Set[State]
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

      override def neighborStatesOfPlayer(stateName: String): Set[String] = gameMap.neighborStatesOfPlayer(stateName, currentPlayer)
      override def neighborStatesOfEnemies(stateName: String): Set[String] = gameMap.neighborStatesOfEnemies(stateName, currentPlayer)

      override def currentPlayerStates: Set[State] =
        gameMap.playerStates(currentPlayer)

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
          gameMap.calcTanksToPlace(currentPlayer)
        }
      }

      override def deployTroops(): Unit = println("troop deployed")

      override def players: Set[Player] = turnManager.get.all

      override def allStates: Set[State] = gameMap.nodes


      override def addTank(stateName: String): Unit =
        if(currentPlayer.equals(gameMap.stateByName(stateName).player) && currentPlayer.tanksToPlace > 0)
          gameMap.stateByName(stateName).addTanks(1)
          currentPlayer.setTanksToPlace(currentPlayer.tanksToPlace-1)

      override def tanksToPlace: Int = currentPlayer.tanksToPlace
      override def switchTurnPhaseActionAvailable :  Set[RisikoAction] = turnPhasesManager.permittedAction

      override def switchPhase(a: RisikoSwitchPhaseAction): Unit = a match
        case EndTurn => turnPhasesManager.trigger(a); turnManager.get.next(); gameMap.calcTanksToPlace(currentPlayer)
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
        attacker.removeTanks(wagonlostAttacker)
        defender.removeTanks(wagonlostDefender)

      override def attackResult(attackerState: State, defenderState: State): Unit = {
        if (attackerState.numberOfTanks > 1 && defenderState.numberOfTanks == 0) {
          defenderState.setPlayer(attackerState.player)
          if(checkWinner())
            throw new MyCustomException("""<html>Great, you are the Winner <br>""")
          throw new MyCustomException("""<html>Great, you conquered <br>""" + defenderState.name)
        }
        else if (attackerState.numberOfTanks == 1) {
          throw new MyCustomException("""<html>Sorry, but you can't attack <br>because you have only one wagon <br> in """ + defenderState.name + """</html>""".stripMargin)
        }
      }

      private var rollDiceAttack = Seq[Int]()
      private var rollDiceDefender = Seq[Int]()

      override def rollDice(typeOfPlayer: String, state: State): Seq[Int] = {
        var numberOfDice: Int = 0;
        var resultRollDice = Seq[Int]()
        if (typeOfPlayer.equals("attack")) {
          if (state.numberOfTanks > 3) {
            numberOfDice = 3
          } else {
            numberOfDice = state.numberOfTanks - 1
          }
          resultRollDice = Seq.fill(numberOfDice)(Random.nextInt(6) + 1).sorted.reverse
          rollDiceAttack = resultRollDice
        }
        else {
          if (state.numberOfTanks >= 3) {
            numberOfDice = 3;
          } else {
            numberOfDice = state.numberOfTanks;
          }
          resultRollDice = Seq.fill(numberOfDice)(Random.nextInt(6) + 1).sorted.reverse
          rollDiceDefender = resultRollDice
        }
        resultRollDice
      }

      override def numberOfDiceForPlayers(attackerState: State, defenderState: State): (Int, Int) = {
        var numberOfDiceAttack = 0
        var numberOfDiceDefender = 0
        if (attackerState.numberOfTanks > 3) {
          numberOfDiceAttack = 3;
        } else {
          numberOfDiceAttack = attackerState.numberOfTanks - 1;
        }

        if (defenderState.numberOfTanks >= 3) {
          numberOfDiceDefender = 3;
        } else {
          numberOfDiceDefender = defenderState.numberOfTanks;
        }
        (numberOfDiceAttack, numberOfDiceDefender)
      }

      override def numberOfRollDiceAttack(): Int = rollDiceAttack.size

      override def moveTanks(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit =
        gameMap.moveTanks(fromStateName, toStateName, numberOfWagon)

      private def checkWinner(): Boolean = currentPlayerStates.size >= 24

      override def currentPhase: RisikoPhase =
        turnPhasesManager.currentPhase

  trait Interface extends Provider with Component:
    self: Requirements =>

