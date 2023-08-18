package model
import controller.ControllerModule
import view.ViewModule.Requirements

import scala.util.Random
import controller.ControllerModule
import model.manager.RisikoRequest.*
import model.manager.RisikoPhase.*
import model.config.SetupFromFiles
import model.entity.{Goal, Player, PlayerColor}
import model.entity.map.{GameMap, State}
import model.manager.attackmanager.{AttackManager, MessageAttackPhase}
import model.manager.{GameSettingManager, RisikoAction, RisikoPhase, RisikoSwitchPhaseAction, SettingResult, TurnManager, TurnPhasesManager, VersionMap}
import view.ViewModule.Requirements

/**
 * The module that represent the Model
 */
object ModelModule:

  /**
   * What the module provide to the other module
   */
  trait Provider:
    val model: Model

  /**
   * The module component used by the provider
   */
  trait Component:

    import java.io.File
    import scala.io.Source
    import model.manager.RisikoSwitchPhaseAction.*

    class ModelImpl extends Model:
      private var gameMap = new GameMap()
      private var attackManager = AttackManager(gameMap)
      private var turnManager : Option[TurnManager[Player]] = None
      private var turnPhasesManager = TurnPhasesManager()
      private var gameSettingManager = GameSettingManager()
      private var _goal : Option[Goal] = None
      
      override def neighborStatesOfPlayer(stateName: String): Set[String] = gameMap.neighborStatesOfPlayer(stateName, currentPlayer)
      override def neighborStatesOfEnemies(stateName: String): Set[String] = gameMap.neighborStatesOfEnemies(stateName, currentPlayer)

      override def currentPlayerStates: Set[State] =
        gameMap.playerStates(currentPlayer)

      override def currentPlayer: Player = turnManager.get.current

      override def stateByName(stateName: String): State = gameMap.stateByName(stateName)

      override def setGameSettings(inputDataPlayer: List[(String, String)], versionOfMap:String): SettingResult =
        val message=gameSettingManager.setGameSettings(inputDataPlayer, versionOfMap)
        if(message.equals(SettingResult.CorrectSettings)){
          SetupFromFiles.setup(gameMap, typeOfMap())
          turnManager = Some(TurnManager(inputDataPlayer.map(element =>
            Player(element._1, PlayerColor.valueOf(element._2))
          )))
          turnManager.get.next()
          gameMap.assignStatesToPlayers(turnManager.get.all,typeOfMap())
          gameMap.calcTanksToPlace(currentPlayer)
          _goal = if typeOfMap() == VersionMap.Europe then Some(Goal("Conquer 13 States")) else Some(Goal("Conquer 24 States"))
        }
        message

      override def players: List[Player] = turnManager.get.all

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

      override def executeAttack(): Unit = attackManager.executeAttack(typeOfMap())

      override def resultOfAttack(): MessageAttackPhase = attackManager.resultMessage
      override def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit =
        gameMap.moveTanks(fromStateName, toStateName, numberOfTanks)
        if(turnPhasesManager.currentPhase == Move){
          turnPhasesManager.trigger(MoveRequest)
          turnManager.get.next()
          gameMap.calcTanksToPlace(currentPlayer)
        }

      override def rollDiceAttacker(): Seq[Int] = attackManager.rollDiceAttacker

      override def rollDiceDefender(): Seq[Int] = attackManager.rollDiceDefender

      override def numberOfTanksToMove(): Seq[Int] = attackManager.numberOfTanksToMove()

      override def setAttackerState(state: State): Unit = attackManager.setAttacker(state)

      override def setDefenderState(state: State): Unit = attackManager.setDefender(state)

      override def setDefaultAttackSettings: Unit = attackManager.setDefaultSettings

      override def setDefaultInitialSettings(): Unit =
        gameMap = new GameMap()
        attackManager = AttackManager(gameMap)
        turnPhasesManager = TurnPhasesManager()
        gameSettingManager = GameSettingManager()
        

      override def typeOfMap(): VersionMap = gameSettingManager.typeOfMap()

      override def attackerState(): State = attackManager.attacker

      override def defenderState(): State = attackManager.defender

      override def numberOfDiceAttacker(): Int = attackManager.numberOfDiceAttacker()

      override def numberOfDiceDefender(): Int = attackManager.numberOfDiceDefender()

      override def currentPhase: RisikoPhase =
        turnPhasesManager.currentPhase

      override def goal = _goal.get

  /**
   * Module interface for mixing the module with the others MVC modules
   */
  trait Interface extends Provider with Component

