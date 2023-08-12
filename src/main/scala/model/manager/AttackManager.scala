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
  def diceRollAttacker: DiceRoll
  def diceRollDefender: DiceRoll
  def resultMessage: MessageAttackPhase
  def setDefaultSettings:Unit
  def setAttacker(state:State):Unit
  def setDefender(state:State):Unit
  def numberOfDice(attacker: State, defender: State): CountUsersDice
  def numberOfTanksToMove(attacker:State):Int


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

    override def numberOfDice(attacker: State, defender: State): CountUsersDice =
      val MaxNumberOfDiceToAttack=3
      val MaxNumberOfDiceToDefence=3

      (attacker, defender) match
      case (attacker, defender) if attacker.numberOfTanks > MaxNumberOfDiceToAttack && defender.numberOfTanks >= MaxNumberOfDiceToDefence => (MaxNumberOfDiceToAttack, MaxNumberOfDiceToDefence)
      case (attacker, defender) if attacker.numberOfTanks > MaxNumberOfDiceToAttack && defender.numberOfTanks < MaxNumberOfDiceToDefence => (MaxNumberOfDiceToAttack, defender.numberOfTanks)
      case (attacker, defender) if attacker.numberOfTanks <= MaxNumberOfDiceToAttack && defender.numberOfTanks >= MaxNumberOfDiceToDefence => (attacker.numberOfTanks - 1, MaxNumberOfDiceToDefence)
      case (attacker, defender) if attacker.numberOfTanks <= MaxNumberOfDiceToAttack && defender.numberOfTanks < MaxNumberOfDiceToDefence => (attacker.numberOfTanks - 1, defender.numberOfTanks)
      case _ => null
    private def rollDicePhase(typeOfPlayer: String): DiceRoll = typeOfPlayer match
      case "attacker" => Seq.fill(numberOfDiceAttacker)(Random.nextInt(6) + 1).sorted.reverse
      case "defender"=> Seq.fill(numberOfDiceDefender)(Random.nextInt(6) + 1).sorted.reverse
      case _ => null

    private def attackProcess(diceAttack: DiceRoll, diceDefence: DiceRoll): Unit = {
      val (wagonLostAttacker, wagonLostDefender) = diceAttack.sorted.reverse.zip(diceDefence.sorted.reverse).foldLeft((0,0)) {
        case ((lostAttacker, lostDefender), (attackerRoll, defenderRoll)) =>
          if (attackerRoll > defenderRoll)
            (lostAttacker, lostDefender+1)
          else if (attackerRoll <= defenderRoll)
            (lostAttacker+1, lostDefender)
          else
            (lostAttacker,lostDefender)
      }

      attackerState.removeTanks(wagonLostAttacker)
      defenderState.removeTanks(wagonLostDefender)
    }

    private def attackResult(attacker: State, defender: State,typeOfMap:VersionMap): MessageAttackPhase =
      val NumberOfStateForWinClassicVersion= 24
      val NumberOfStateForWinEuropeanVersion=13
      (attacker.numberOfTanks, defender.numberOfTanks, typeOfMap) match
      case (attackerTanks, 0, _) if attackerTanks > 1 =>
        defender.setPlayer(attacker.player)
        typeOfMap match
          case VersionMap.Classic if gameMap.playerStates(attacker.player).size >= NumberOfStateForWinClassicVersion => MessageAttackPhase.Winner
          case VersionMap.Classic if gameMap.playerStates(attacker.player).size < NumberOfStateForWinClassicVersion => MessageAttackPhase.ConqueredState
          case VersionMap.Europe if gameMap.playerStates(attacker.player).size >=NumberOfStateForWinEuropeanVersion => MessageAttackPhase.Winner
          case VersionMap.Europe if gameMap.playerStates(attacker.player).size < NumberOfStateForWinEuropeanVersion => MessageAttackPhase.ConqueredState
      case (attackerTanks, _,_) if attackerTanks ==1 => MessageAttackPhase.LoseAttack
      case _ => MessageAttackPhase.ContinueAttack


    override def attackPhase(typeOfMap:VersionMap): Unit =
      val (attackerDice,defenderDice)=numberOfDice(attackerState, defenderState)
      numberOfDiceDefender=defenderDice
      numberOfDiceAttacker=attackerDice
      resultDiceRollAttacker=rollDicePhase("attacker")
      resultDiceRollDefender=rollDicePhase("defender")
      attackProcess(resultDiceRollAttacker,resultDiceRollDefender)
      message=attackResult(attackerState,defenderState, typeOfMap)

    override def numberOfTanksToMove(attacker: State): Int = attacker match
      case stateAttacker if (stateAttacker.numberOfTanks - 1).equals(numberOfDiceAttacker) => numberOfDiceAttacker
      case _ => attacker.numberOfTanks
    override def attacker: State = attackerState

    override def defender: State = defenderState

    override def diceRollAttacker: DiceRoll = resultDiceRollAttacker

    override def diceRollDefender: DiceRoll = resultDiceRollDefender

    override def resultMessage: MessageAttackPhase = message

    override def setAttacker(state: State): Unit = attackerState=state

    override def setDefender(state: State): Unit = defenderState=state

    override def setDefaultSettings: Unit =
      attackerState=null
      defenderState=null
      resultDiceRollAttacker= null
      resultDiceRollDefender= null
      numberOfDiceAttacker= 0
      numberOfDiceDefender= 0
      message= null