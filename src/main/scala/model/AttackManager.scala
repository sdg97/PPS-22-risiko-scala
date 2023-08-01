package model

import scala.collection.immutable.Seq
import scala.util.Random

enum MessageAttackPhase:
  case ContinueAttack
  case ConqueredState
  case LoseAttack
  case Winner

trait AttackManager:
  def attack(typeOfMap:VersionMap): Unit
  def getAttacker: State
  def getDefender:State
  def getRollDiceAttacker: Seq[Int]
  def getRollDiceDefender: Seq[Int]
  def getMessage: MessageAttackPhase
  def setDefaultSettings:Unit
  def setAttacker(state:State):Unit
  def setDefender(state:State):Unit
  def getNumberOfDice(attacker: State, defender: State): (Int, Int)
  def numberOfTanksToMove(attacker:State):Int

//esiste solo quell'implementazione valida
object AttackManager:
  def apply(gameMap: GameMap):AttackManager=AttackManagerImpl(gameMap)

  private class AttackManagerImpl(gameMap: GameMap) extends AttackManager:

    private var attackerState:State=null
    private var defenderState:State=null
    private var rollDiceAttacker: Seq[Int] = null
    private var rollDiceDefender: Seq[Int] = null
    private var numberOfRollDiceAttacker: Int = 0
    private var numberOfRollDiceDefender: Int = 0
    private var message:MessageAttackPhase= null

    override def getNumberOfDice(attacker: State, defender: State): (Int, Int) = (attacker, defender) match
      case (attacker, defender) if attacker.numberOfTanks > 3 && defender.numberOfTanks >= 3 => (3, 3)
      case (attacker, defender) if attacker.numberOfTanks > 3 && defender.numberOfTanks < 3 => (3, defender.numberOfTanks)
      case (attacker, defender) if attacker.numberOfTanks <= 3 && defender.numberOfTanks >= 3 => (attacker.numberOfTanks - 1, 3)
      case (attacker, defender) if attacker.numberOfTanks <= 3 && defender.numberOfTanks < 3 => (attacker.numberOfTanks - 1, defender.numberOfTanks)
      case _ => null
    private def rollDice(typeOfPlayer: String, state: State): Seq[Int] = (typeOfPlayer, state) match
      case ("attacker", state) if state.numberOfTanks > 3 =>
        Seq.fill(numberOfRollDiceAttacker)(Random.nextInt(6) + 1).sorted.reverse
      case ("attacker", state) if state.numberOfTanks <= 3 =>
        Seq.fill(numberOfRollDiceAttacker)(Random.nextInt(6) + 1).sorted.reverse
      case ("defender", state) if state.numberOfTanks >= 3 =>
        Seq.fill(numberOfRollDiceDefender)(Random.nextInt(6) + 1).sorted.reverse
      case ("defender", state) if state.numberOfTanks < 3 =>
        Seq.fill(numberOfRollDiceDefender)(Random.nextInt(6) + 1).sorted.reverse
      case _=>null

    private def attackProcess(diceAttack: Seq[Int], diceDefence: Seq[Int]): (Int, Int) = {
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

    private def attackResult(attacker: State, defender: State,typeOfMap:VersionMap): MessageAttackPhase = (attacker, defender, typeOfMap) match
      case (attacker, defender, mapGame) if attacker.numberOfTanks > 1 && defender.numberOfTanks == 0 =>
        defender.setPlayer(attacker.player)
        mapGame match
          case VersionMap.Classic if gameMap.playerStates(attacker.player).size >= 24 => MessageAttackPhase.Winner
          case VersionMap.Classic if gameMap.playerStates(attacker.player).size < 24 => MessageAttackPhase.ConqueredState
          case VersionMap.Europe if gameMap.playerStates(attacker.player).size >=13 => MessageAttackPhase.Winner
          case VersionMap.Europe if gameMap.playerStates(attacker.player).size <13 => MessageAttackPhase.ConqueredState
      case (attacker, defender,map) if attacker.numberOfTanks ==1 => MessageAttackPhase.LoseAttack
      case _ => MessageAttackPhase.ContinueAttack


    override def attack(typeOfMap:VersionMap): Unit =
      numberOfRollDiceAttacker=getNumberOfDice(attackerState, defenderState)._1
      numberOfRollDiceDefender=getNumberOfDice(attackerState, defenderState)._2
      rollDiceAttacker=rollDice("attacker", attackerState)
      rollDiceDefender=rollDice("defender", defenderState)
      val result=attackProcess(rollDiceAttacker,rollDiceDefender)
      attackerState.removeTanks(result._1)
      defenderState.removeTanks(result._2)
      message=attackResult(attackerState,defenderState, typeOfMap)

    override def numberOfTanksToMove(attacker: State): Int = attacker match
      case attacker if (attacker.numberOfTanks - 1).equals(numberOfRollDiceAttacker) => numberOfRollDiceAttacker
      case _ => attacker.numberOfTanks
    override def getAttacker: State = attackerState

    override def getDefender: State = defenderState

    override def getRollDiceAttacker: Seq[Int] = rollDiceAttacker

    override def getRollDiceDefender: Seq[Int] = rollDiceDefender

    override def getMessage: MessageAttackPhase = message

    override def setAttacker(state: State): Unit = attackerState=state

    override def setDefender(state: State): Unit = defenderState=state

    override def setDefaultSettings: Unit =
      rollDiceAttacker= null
      rollDiceDefender= null
      numberOfRollDiceAttacker= 0
      numberOfRollDiceDefender= 0
      message= null



