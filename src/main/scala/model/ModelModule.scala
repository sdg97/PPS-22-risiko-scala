package model
import controller.ControllerModule
import view.ViewModule.Requirements

import scala.util.Random
import controller.ControllerModule
import model.manager.RisikoRequest.*
import model.manager.RisikoPhase.*
import model.config.SetupFromFiles
import model.entity.{Player, PlayerColor}
import model.entity.map.{GameMap, State}
import model.manager.{AttackManager, GameSettingManager, MessageAttackPhase, MessageSetting, RisikoAction, RisikoPhase, RisikoSwitchPhaseAction, TurnManager, TurnPhasesManager, VersionMap}
import view.ViewModule.Requirements

object ModelModule:

  trait Provider:
    val model: Model

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

      override def attack(): Unit = attackManager.attackPhase(setTypeOfMap())

      override def attackResult(): MessageAttackPhase = attackManager.resultMessage

      override def numberOfDiceForPlayers(attacker: State, defender: State): (Int, Int) = attackManager.numberOfDice(attacker, defender)


      override def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit =
        gameMap.moveTanks(fromStateName, toStateName, numberOfTanks)
        println(turnManager.get.current)
        if(turnPhasesManager.currentPhase == Move){
          turnPhasesManager.trigger(MoveRequest)
          turnManager.get.next()
          gameMap.calcTanksToPlace(currentPlayer)
        }

      private def checkWinner(): Boolean = currentPlayerStates.size >= 24

      override def rollDiceAttacker(): Seq[Int] = attackManager.rollDiceAttacker

      override def rollDiceDefender(): Seq[Int] = attackManager.rollDiceDefender

      override def numberOfTanksToMove(attacker: State): Int = attackManager.numberOfTanksToMove(attacker)

      override def setAttacker(state: State): Unit = attackManager.setAttacker(state)

      override def setDefender(state: State): Unit = attackManager.setDefender(state)

      override def setDefaultAttackSettings: Unit = attackManager.setDefaultSettings

      override def setDefaultInitialSettings(): Unit =
        gameMap = new GameMap()
        attackManager = manager.AttackManager(gameMap)
        turnPhasesManager = TurnPhasesManager()
        gameSettingManager = GameSettingManager()
        

      override def setTypeOfMap(): VersionMap = gameSettingManager.getTypeOfMap()

      override def getAttacker(): State = attackManager.attacker

      override def getDefender(): State = attackManager.defender

      override def currentPhase: RisikoPhase =
        turnPhasesManager.currentPhase

  trait Interface extends Provider with Component

