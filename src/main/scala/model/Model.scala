package model
import controller.ControllerModule
import view.ViewModule.Requirements

import scala.util.Random
import controller.ControllerModule
import model.RisikoRequest.*
import model.RisikoPhase.*
import utils.SetupFromFiles
import view.ViewModule.Requirements

object ModelModule:
  trait Model {

    def setGameSettings(inputDataPlayer: Set[(String, String)], typeOfMap:String): MessageSetting
    def players: Set[Player]
    def deployTroops(): Unit
    
    def getAttacker():State
    def getDefender():State

    /**
     * @param stateName the state's name from which to search for neighbors
     * @return a Set of the names of neighboring states with the state passed as a parameter and owned by enemy players
     */
    def neighborStatesOfEnemies(stateName: String): Set[String]

    /**
     * @param stateName the state's name from which to search for neighbors
     * @return a Set of the names of neighboring states with the state passed as a parameter and owned by the player passed as a parameter
     */
    def neighborStatesOfPlayer(stateName: String): Set[String]

    /**
     *
     * @return a Set of all states owned by the current player
     */
    def currentPlayerStates: Set[State]

    /**
     *
     * @return a Set of all the states
     */
    def allStates: Set[State]
    def attack(): Unit
    def attackResult(): MessageAttackPhase
    def rollDiceAttacker(): Seq[Int]
    def rollDiceDefender(): Seq[Int]

    def setAttacker(state: State): Unit

    def setDefender(state: State): Unit
    def numberOfDiceForPlayers(attacker: State, defender: State):(Int,Int)
    def numberOfTanksToMove(attacker:State):Int

    /**
     *
     * @return the current player
     */
    def currentPlayer: Player

    /**
     * @param stateName the name of the state
     * @return the state object
     */
    def stateByName(stateName: String): State

    /**
     * @param stateName the name of the state to assign the tanks to
     * @return a Set of all states owned by the current player
     */
    def addTank(stateName: String): Unit

    /**
     * @return the number of tanks that the current player can place.
     */
    def tanksToPlace: Int
    def switchTurnPhaseActionAvailable : Set[RisikoAction]
    def switchPhase(a: RisikoSwitchPhaseAction): Unit
    def currentPhase: RisikoPhase

    /**
     *
     * moves tanks from a state to another state
     *
     * @param fromStateName starting state
     * @param toStateName   arrival status
     * @param numberOfTanks the number of tanks to move
     */
    def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit
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
        if(currentPlayer.equals(gameMap.stateByName(stateName).player) && currentPlayer.getTanksToPlace > 0)
          gameMap.stateByName(stateName).addTanks(1)
          currentPlayer.setTanksToPlace(currentPlayer.getTanksToPlace-1)

      override def tanksToPlace: Int = currentPlayer.getTanksToPlace
      override def switchTurnPhaseActionAvailable :  Set[RisikoAction] = turnPhasesManager.permittedAction

      override def switchPhase(a: RisikoSwitchPhaseAction): Unit = a match
        case EndTurn => turnPhasesManager.trigger(a); turnManager.get.next(); gameMap.calcTanksToPlace(currentPlayer)
        case _ => turnPhasesManager.trigger(a)

      override def attack(): Unit = attackManager.attack(setTypeOfMap())

      override def attackResult(): MessageAttackPhase = attackManager.getMessage

      override def numberOfDiceForPlayers(attacker: State, defender: State): (Int, Int) = attackManager.getNumberOfDice(attacker, defender)


      override def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit =
        gameMap.moveTanks(fromStateName, toStateName, numberOfTanks)
        println(turnManager.get.current)
        if(turnPhasesManager.currentPhase == Move){
          turnPhasesManager.trigger(MoveRequest)
          turnManager.get.next()
          gameMap.calcTanksToPlace(currentPlayer)
        }

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

      override def getAttacker(): State = attackManager.getAttacker

      override def getDefender(): State = attackManager.getDefender

      override def currentPhase: RisikoPhase =
        turnPhasesManager.currentPhase

  trait Interface extends Provider with Component:
    self: Requirements =>

