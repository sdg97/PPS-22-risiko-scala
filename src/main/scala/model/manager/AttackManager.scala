package model.manager

import model.*
import model.entity.map.{GameMap, State}

import scala.collection.immutable.Seq
import scala.util.Random

enum MessageAttackPhase:
  case ContinueAttack
  case ConqueredState
  case LoseAttack
  case Winner

trait AttackManager:
  type DiceRoll=Seq[Int]
  type CountUsersDice=(Int,Int)
  def attackPhase(typeOfMap:VersionMap): Unit
  def attacker: State
  def defender:State
  def setDiceRollAttacker(diceRollAttacker:DiceRoll):Unit
  def setDiceRollDefender(diceRollDefender:DiceRoll):Unit
  def setNumberOfDiceAttacker(numberOfDice:Int):Unit
  def setNumberOfDiceDefender(numberOfDice:Int):Unit
  def diceRollAttacker: DiceRoll
  def diceRollDefender: DiceRoll
  def resultMessage: MessageAttackPhase
  def setResultMessage(messageAttack:MessageAttackPhase): Unit
  def setDefaultSettings:Unit
  def setAttacker(state:State):Unit
  def setDefender(state:State):Unit
  def numberOfDice(attacker: State, defender: State): CountUsersDice
  def numberOfTanksToMove():Int
  def setNumberOfTanksToMove(tanksToMove:Int):Unit


object AttackManager:
  def apply(gameMap: GameMap):AttackManager=AttackManagerImpl(gameMap)

  private class AttackManagerImpl(gameMap: GameMap) extends AttackManager:

    private var attackerState:State=null
    private var defenderState:State=null
    private var resultDiceRollAttacker: DiceRoll = null
    private var resultDiceRollDefender: DiceRoll = null
    private var numberOfDiceAttacker: Int = 0
    private var numberOfDiceDefender: Int = 0
    private var message:MessageAttackPhase= null
    private var tanksToMove:Int=0;

    private var currentPhase: AttackPhase = _

    private def transitionToNextAttackPhase(phase: AttackPhase): Unit = {
      currentPhase = phase
      executeCurrentPhase(this)
    }

    private def executeCurrentPhase(attackManager: AttackManager): Unit = {
      if (currentPhase != null) {
        currentPhase.execute(attackManager)
      }
    }

    override def numberOfDice(attacker: State, defender: State): CountUsersDice =
      val MaxNumberOfDiceToAttack=3
      val MaxNumberOfDiceToDefence=3

      (attacker, defender) match
        case (attacker, defender) if attacker.numberOfTanks > MaxNumberOfDiceToAttack && defender.numberOfTanks >= MaxNumberOfDiceToDefence => (MaxNumberOfDiceToAttack, MaxNumberOfDiceToDefence)
        case (attacker, defender) if attacker.numberOfTanks > MaxNumberOfDiceToAttack && defender.numberOfTanks < MaxNumberOfDiceToDefence => (MaxNumberOfDiceToAttack, defender.numberOfTanks)
        case (attacker, defender) if attacker.numberOfTanks <= MaxNumberOfDiceToAttack && defender.numberOfTanks >= MaxNumberOfDiceToDefence => (attacker.numberOfTanks - 1, MaxNumberOfDiceToDefence)
        case (attacker, defender) if attacker.numberOfTanks <= MaxNumberOfDiceToAttack && defender.numberOfTanks < MaxNumberOfDiceToDefence => (attacker.numberOfTanks - 1, defender.numberOfTanks)
        case _ => null

    override def attackPhase(typeOfMap:VersionMap): Unit =
      transitionToNextAttackPhase(new RollingDicePhase)
      transitionToNextAttackPhase(new DiceComparisonPhase)
      transitionToNextAttackPhase(new OutcomeAttackPhase(typeOfMap, gameMap))
      if(message==MessageAttackPhase.ConqueredState)
        transitionToNextAttackPhase(new MovementInConquaredStatePhase)
    override def attacker: State = attackerState

    override def defender: State = defenderState

    override def diceRollAttacker: DiceRoll = resultDiceRollAttacker

    override def diceRollDefender: DiceRoll = resultDiceRollDefender

    override def resultMessage: MessageAttackPhase = message

    override def setAttacker(state: State): Unit = attackerState=state

    override def setDefender(state: State): Unit = defenderState=state

    override def setDiceRollAttacker(diceRollAttacker: DiceRoll): Unit = resultDiceRollAttacker=diceRollAttacker

    override def setDiceRollDefender(diceRollDefender: DiceRoll): Unit = resultDiceRollDefender=diceRollDefender

    override def setNumberOfDiceAttacker(numberOfDice: Int): Unit = numberOfDiceAttacker=numberOfDice

    override def setNumberOfDiceDefender(numberOfDice: Int): Unit = numberOfDiceDefender=numberOfDice

    override def setResultMessage(messageAttack: MessageAttackPhase): Unit = message=messageAttack

    override def numberOfTanksToMove(): Int = tanksToMove

    override def setNumberOfTanksToMove(tanks: Int): Unit = tanksToMove=tanks

    override def setDefaultSettings: Unit =
      attackerState=null
      defenderState=null
      resultDiceRollAttacker= null
      resultDiceRollDefender= null
      numberOfDiceAttacker= 0
      numberOfDiceDefender= 0
      message= null