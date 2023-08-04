package model
import controller.ControllerModule
import view.ViewModule.Requirements

import scala.util.Random
import controller.ControllerModule
import model.RisikoRequest.MoveRequest
import utils.SetupFromFiles
import view.ViewModule.Requirements

object ModelModule:
  trait Model {

    def setGameSettings(inputDataPlayer: Set[(String, String)], typeOfMap:String): MessageSetting
    def players: Set[Player]
    def deployTroops(): Unit
    def neighborStatesOfEnemies(stateName: String): Set[String]
    def neighborStatesOfPlayer(state: String): Set[String]
    def currentPlayerStates: Set[State]
    def allStates: Set[State]
    def attack(): Unit
    def attackResult(): MessageAttackPhase
    def rollDiceAttacker(): Seq[Int]
    def rollDiceDefender(): Seq[Int]

    def setAttacker(state: State): Unit

    def setDefender(state: State): Unit
    def numberOfDiceForPlayers(attacker: State, defender: State):(Int,Int)
    def numberOfTanksToMove(attacker:State):Int
    def currentPlayer: Player
    def stateByName(stateName: String): State
    def addTank(stateName: String): Unit
    def tanksToPlace: Int
    def switchTurnPhaseActionAvailable : Set[RisikoAction]
    def switchPhase(a: RisikoSwitchPhaseAction): Unit
    def currentPhase: RisikoPhase
    def moveTanks(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit
    def setDefaultAttackSettings:Unit
    def setDefaultInitialSettings():Unit
    def setTypeOfMap():VersionMap
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
      private var gameMap = new GameMap()
      private var attackManager = AttackManager(gameMap)
      private var turnManager : Option[TurnManager[Player]] = None
      private var turnPhasesManager = TurnPhasesManager()
      private var gameSettingManager = GameSettingManager()
      

      override def neighborStatesOfPlayer(stateName: String): Set[String] = gameMap.neighborStatesOfPlayer(stateName, currentPlayer)
      override def neighborStatesOfEnemies(stateName: String): Set[String] = gameMap.neighborStatesOfEnemies(stateName, currentPlayer)

      override def currentPlayerStates: Set[State] =
        gameMap.playerStates(currentPlayer)

      override def currentPlayer: Player = turnManager.get.current

      override def stateByName(stateName: String): State = gameMap.stateByName(stateName)

      override def setGameSettings(inputDataPlayer: Set[(String, String)], typeOfMap:String): MessageSetting =
        val message=gameSettingManager.setGameSettings(inputDataPlayer, typeOfMap)
        if(message.equals(MessageSetting.CorrectSettings)){
          SetupFromFiles.setup(gameMap, setTypeOfMap())
          turnManager = Some(TurnManager(inputDataPlayer.map(element =>
            Player(element._1, PlayerColor.valueOf(element._2))
          )))
          turnManager.get.next()
          gameMap.assignStatesToPlayers(turnManager.get.all,setTypeOfMap())
          gameMap.calcTanksToPlace(currentPlayer)
        }
        message

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

      override def attack(): Unit = attackManager.attack(setTypeOfMap())

      override def attackResult(): MessageAttackPhase = attackManager.getMessage

      override def numberOfDiceForPlayers(attacker: State, defender: State): (Int, Int) = attackManager.getNumberOfDice(attacker, defender)


      override def moveTanks(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit =
        gameMap.moveTanks(fromStateName, toStateName, numberOfWagon)
        turnPhasesManager.trigger(MoveRequest)
        turnManager.get.next()

      private def checkWinner(): Boolean = currentPlayerStates.size >= 24

      override def rollDiceAttacker(): Seq[Int] = attackManager.getRollDiceAttacker

      override def rollDiceDefender(): Seq[Int] = attackManager.getRollDiceDefender

      override def numberOfTanksToMove(attacker: State): Int = attackManager.numberOfTanksToMove(attacker)

      override def setAttacker(state: State): Unit = attackManager.setAttacker(state)

      override def setDefender(state: State): Unit = attackManager.setDefender(state)

      override def setDefaultAttackSettings: Unit = attackManager.setDefaultSettings

      override def setDefaultInitialSettings(): Unit =
        gameMap = new GameMap()
        attackManager = AttackManager(gameMap)
        turnPhasesManager = TurnPhasesManager()
        gameSettingManager = GameSettingManager()
        

      override def setTypeOfMap(): VersionMap = gameSettingManager.getTypeOfMap()

      override def currentPhase: RisikoPhase =
        turnPhasesManager.currentPhase

  trait Interface extends Provider with Component:
    self: Requirements =>

