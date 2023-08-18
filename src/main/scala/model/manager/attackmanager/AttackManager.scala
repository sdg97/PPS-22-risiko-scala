package model.manager.attackmanager

import model.*
import model.entity.map.{GameMap, State}
import model.manager.*

import scala.collection.immutable.Seq
import scala.util.Random

/**
 * It specify the different scenarios that could be happen at the end of attack phase
 */
enum MessageAttackPhase:
  case ContinueAttack
  case ConqueredState
  case LoseAttack
  case Winner

type DiceRoll = Seq[Int]
type CountUsersDice = (Int, Int)
trait AttackManager:
  /**
   * method to execute the attack process.
   *
   * @param typeOfMap the typology of map chose by the players.
   */
  def executeAttack(typeOfMap:VersionMap): Unit

  /**
   *
   * @return the state from which starts the attack.
   */
  def attacker: State

  /**
   *
   * @return the attacked state.
   */
  def defender:State

  /**
   * method to set the result of roll dice by attacker player.
   *
   * @param diceRollAttacker the result of roll dice by attacker player.
   */
  def setDiceRollAttacker(diceRollAttacker:DiceRoll):Unit

  /**
   * method to set the result of roll dice by defender player.
   *
   * @param diceRollDefender the result of roll dice by defender player.
   */
  def setDiceRollDefender(diceRollDefender:DiceRoll):Unit

  /**
   *
   * @return the result of roll dice by attacker player.
   */
  def rollDiceAttacker: DiceRoll

  /**
   *
   * @return the result of roll dice by defender player.
   */
  def rollDiceDefender: DiceRoll

  /**
   *
   * @return the message about different scenarios that could be happen at the end of attack phase.
   */
  def resultMessage: MessageAttackPhase

  /**
   * method to set the message about different scenarios that could be happen at the end of attack phase.
   *
   * @param messageAttack the message about different scenarios that could be happen at the end of attack phase.
   */
  def setResultMessage(messageAttack:MessageAttackPhase): Unit

  /**
   * method to set the default initial settings of attack phase.
   *
   */
  def setDefaultSettings:Unit

  /**
   * method to set state from which starts the attack.
   *
   * @param state state from which starts the attack.
   */
  def setAttacker(state:State):Unit

  /**
   * method to set the attacked state.
   *
   * @param state the attacked state.
   */
  def setDefender(state:State):Unit

  /**
   * @return the number of tanks to move in conquered state
   *
   */
  def numberOfTanksToMove():Int

  /**
   * method to set the tanks to move in conquered state
   *
   * @param tanksToMove the number of tanks to move in conquered state
   */
  def setNumberOfTanksToMove(tanksToMove:Int):Unit

  /**
   * @return the number of attacker player's dice
   *
   */
  def numberOfDiceAttacker(): Int

  /**
   * @return the number of defender player's dice
   *
   */
  def numberOfDiceDefender(): Int


object AttackManager:
  def apply(gameMap: GameMap):AttackManager=AttackManagerImpl(gameMap)

  private class AttackManagerImpl(gameMap: GameMap) extends AttackManager:

    private var attackerState:State=null
    private var defenderState:State=null
    private var resultDiceRollAttacker: DiceRoll = null
    private var resultDiceRollDefender: DiceRoll = null
    private var numberOfDiceAttackerState: Int = 0
    private var numberOfDiceDefenderState: Int = 0
    private var message:MessageAttackPhase= null
    private var tanksToMove:Int=0;

    private var currentPhase: AttackPhase = _

    /**
     * method to set the current phase of attack.
     * @param phase the phase to transaction
     */
    private def transitionToNextAttackPhase(phase: AttackPhase): Unit = {
      currentPhase = phase
      executeCurrentPhase(this)
    }

    /**
     * method to execute the current phase of attack
     *
     * @param attackManager the manager of attack processes
     */
    private def executeCurrentPhase(attackManager: AttackManager): Unit = {
      if (currentPhase != null) {
        currentPhase.execute(attackManager)
      }
    }

    /**
     * @return the number of dice about attacker and defender.
     *
     */
    private def numberOfDice(): CountUsersDice =
      val MaxNumberOfDiceToAttack=3
      val MaxNumberOfDiceToDefence=3

      (attackerState, defenderState) match
        case (attacker, defender) if attacker.numberOfTanks > MaxNumberOfDiceToAttack && defender.numberOfTanks >= MaxNumberOfDiceToDefence => (MaxNumberOfDiceToAttack, MaxNumberOfDiceToDefence)
        case (attacker, defender) if attacker.numberOfTanks > MaxNumberOfDiceToAttack && defender.numberOfTanks < MaxNumberOfDiceToDefence => (MaxNumberOfDiceToAttack, defender.numberOfTanks)
        case (attacker, defender) if attacker.numberOfTanks <= MaxNumberOfDiceToAttack && defender.numberOfTanks >= MaxNumberOfDiceToDefence => (attacker.numberOfTanks - 1, MaxNumberOfDiceToDefence)
        case (attacker, defender) if attacker.numberOfTanks <= MaxNumberOfDiceToAttack && defender.numberOfTanks < MaxNumberOfDiceToDefence => (attacker.numberOfTanks - 1, defender.numberOfTanks)
        case _ => null

    override def executeAttack(typeOfMap:VersionMap): Unit =
      transitionToNextAttackPhase(new RollingDicePhase)
      transitionToNextAttackPhase(new DiceComparisonPhase)
      transitionToNextAttackPhase(new OutcomeAttackPhase(typeOfMap, gameMap))
      if(message==MessageAttackPhase.ConqueredState)
        transitionToNextAttackPhase(new TankMovementPhase)
    override def attacker: State = attackerState

    override def defender: State = defenderState

    override def rollDiceAttacker: DiceRoll = resultDiceRollAttacker

    override def rollDiceDefender: DiceRoll = resultDiceRollDefender

    override def resultMessage: MessageAttackPhase = message

    override def setAttacker(state: State): Unit = attackerState=state

    override def setDefender(state: State): Unit = defenderState=state

    override def setDiceRollAttacker(diceRollAttacker: DiceRoll): Unit = resultDiceRollAttacker=diceRollAttacker

    override def setDiceRollDefender(diceRollDefender: DiceRoll): Unit = resultDiceRollDefender=diceRollDefender

    override def setResultMessage(messageAttack: MessageAttackPhase): Unit = message=messageAttack

    override def numberOfTanksToMove(): Int = tanksToMove

    override def setNumberOfTanksToMove(tanks: Int): Unit = tanksToMove=tanks

    override def numberOfDiceAttacker(): Int =
      numberOfDiceAttackerState=numberOfDice()._1
      numberOfDiceAttackerState


    override def numberOfDiceDefender(): Int =
      numberOfDiceDefenderState=numberOfDice()._2
      numberOfDiceDefenderState

    override def setDefaultSettings: Unit =
      attackerState=null
      defenderState=null
      resultDiceRollAttacker= null
      resultDiceRollDefender= null
      numberOfDiceAttackerState= 0
      numberOfDiceDefenderState= 0
      message= null