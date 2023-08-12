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
  def attackPhase(typeOfMap:VersionMap): Unit
  def attacker: State
  def defender:State
  def rollDiceAttacker: Seq[Int]
  def rollDiceDefender: Seq[Int]
  def resultMessage: MessageAttackPhase
  def setDefaultSettings:Unit
  def setAttacker(state:State):Unit
  def setDefender(state:State):Unit
  def numberOfDice(attacker: State, defender: State): (Int, Int)
  def numberOfTanksToMove(attacker:State):Int

//esiste solo quell'implementazione valida
object AttackManager:
  def apply(gameMap: GameMap):AttackManager=AttackManagerImpl(gameMap)

  private class AttackManagerImpl(gameMap: GameMap) extends AttackManager:

    private var attackerState:State=null
    private var defenderState:State=null
    private var resultRollDiceAttacker: Seq[Int] = null
    private var resultRollDiceDefender: Seq[Int] = null
    private var numberOfRollDiceAttacker: Int = 0
    private var numberOfRollDiceDefender: Int = 0
    private var message:MessageAttackPhase= null

    override def numberOfDice(attacker: State, defender: State): (Int, Int) = (attacker, defender) match
      case (attacker, defender) if attacker.numberOfTanks > 3 && defender.numberOfTanks >= 3 => (3, 3)
      case (attacker, defender) if attacker.numberOfTanks > 3 && defender.numberOfTanks < 3 => (3, defender.numberOfTanks)
      case (attacker, defender) if attacker.numberOfTanks <= 3 && defender.numberOfTanks >= 3 => (attacker.numberOfTanks - 1, 3)
      case (attacker, defender) if attacker.numberOfTanks <= 3 && defender.numberOfTanks < 3 => (attacker.numberOfTanks - 1, defender.numberOfTanks)
      case _ => null
    def rollDice(typeOfPlayer: String, state: State): Seq[Int] = (typeOfPlayer, state) match
      case ("attacker", state) if state.numberOfTanks > 3 =>
        Seq.fill(numberOfRollDiceAttacker)(Random.nextInt(6) + 1).sorted.reverse
      case ("attacker", state) if state.numberOfTanks <= 3 =>
        Seq.fill(numberOfRollDiceAttacker)(Random.nextInt(6) + 1).sorted.reverse
      case ("defender", state) if state.numberOfTanks >= 3 =>
        Seq.fill(numberOfRollDiceDefender)(Random.nextInt(6) + 1).sorted.reverse
      case ("defender", state) if state.numberOfTanks < 3 =>
        Seq.fill(numberOfRollDiceDefender)(Random.nextInt(6) + 1).sorted.reverse
      case _=>null

    def attackProcess(diceAttack: Seq[Int], diceDefence: Seq[Int]): (Int, Int) = {
      var wagonlostAttacker: Int = 0;
      var wagonlostDefender: Int = 0;
      diceAttack.sorted.reverse.zip(diceDefence.sorted.reverse).map { case (elem1, elem2) =>
        if (elem1 > elem2)
          wagonlostDefender = wagonlostDefender + 1
        else if (elem1 <= elem2)
          wagonlostAttacker = wagonlostAttacker + 1
      }
      (wagonlostAttacker, wagonlostDefender)
    }

    def attackResult(attacker: State, defender: State,typeOfMap:VersionMap): MessageAttackPhase = (attacker, defender, typeOfMap) match
      case (attacker, defender, mapGame) if attacker.numberOfTanks > 1 && defender.numberOfTanks == 0 =>
        defender.setPlayer(attacker.player)
        mapGame match
          case VersionMap.Classic if gameMap.playerStates(attacker.player).size >= 24 => MessageAttackPhase.Winner
          case VersionMap.Classic if gameMap.playerStates(attacker.player).size < 24 => MessageAttackPhase.ConqueredState
          case VersionMap.Europe if gameMap.playerStates(attacker.player).size >=13 => MessageAttackPhase.Winner
          case VersionMap.Europe if gameMap.playerStates(attacker.player).size <13 => MessageAttackPhase.ConqueredState
      case (attacker, defender,map) if attacker.numberOfTanks ==1 => MessageAttackPhase.LoseAttack
      case _ => MessageAttackPhase.ContinueAttack


    override def attackPhase(typeOfMap:VersionMap): Unit =
      numberOfRollDiceAttacker=numberOfDice(attackerState, defenderState)._1
      numberOfRollDiceDefender=numberOfDice(attackerState, defenderState)._2
      resultRollDiceAttacker=rollDice("attacker", attackerState)
      resultRollDiceDefender=rollDice("defender", defenderState)
      val result=attackProcess(resultRollDiceAttacker,resultRollDiceDefender)
      attackerState.removeTanks(result._1)
      defenderState.removeTanks(result._2)
      message=attackResult(attackerState,defenderState, typeOfMap)

    override def numberOfTanksToMove(attacker: State): Int = attacker match
      case attacker if (attacker.numberOfTanks - 1).equals(numberOfRollDiceAttacker) => numberOfRollDiceAttacker
      case _ => attacker.numberOfTanks
    override def attacker: State = attackerState

    override def defender: State = defenderState

    override def rollDiceAttacker: Seq[Int] = resultRollDiceAttacker

    override def rollDiceDefender: Seq[Int] = resultRollDiceDefender

    override def resultMessage: MessageAttackPhase = message

    override def setAttacker(state: State): Unit = attackerState=state

    override def setDefender(state: State): Unit = defenderState=state

    override def setDefaultSettings: Unit =
      attackerState=null
      defenderState=null
      resultRollDiceAttacker= null
      resultRollDiceDefender= null
      numberOfRollDiceAttacker= 0
      numberOfRollDiceDefender= 0
      message= null



