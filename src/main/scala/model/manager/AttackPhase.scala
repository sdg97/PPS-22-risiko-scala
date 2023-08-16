package model.manager

import model.entity.map.{GameMap, State}

import scala.util.Random

trait AttackPhase {
  type DiceRoll = Seq[Int]
  type CountUsersDice = (Int, Int)
  def execute(attackManager: AttackManager): Unit

}

class RollingDicePhase extends AttackPhase{

  private def rollDicePhase(typeOfPlayer: String, numberDice:Int): DiceRoll = typeOfPlayer match
    case "attacker" => Seq.fill(numberDice)(Random.nextInt(6) + 1).sorted.reverse
    case "defender" => Seq.fill(numberDice)(Random.nextInt(6) + 1).sorted.reverse
    case _ => null

  override def execute(attackManager: AttackManager): Unit =
    val (attackerDice, defenderDice) = attackManager.numberOfDice(attackManager.attacker,attackManager.defender)
    attackManager.setNumberOfDiceAttacker(attackerDice)
    attackManager.setNumberOfDiceDefender(defenderDice)
    attackManager.setDiceRollAttacker(rollDicePhase("attacker",attackerDice))
    attackManager.setDiceRollDefender(rollDicePhase("defender",defenderDice))
}

class DiceComparisonPhase extends AttackPhase{

  private def diceComparison(diceAttack: DiceRoll, diceDefence: DiceRoll, attackerState:State, defenderState:State): Unit = {
    val (wagonLostAttacker, wagonLostDefender) = diceAttack.sorted.reverse.zip(diceDefence.sorted.reverse).foldLeft((0, 0)) {
      case ((lostAttacker, lostDefender), (attackerRoll, defenderRoll)) =>
        if (attackerRoll > defenderRoll)
          (lostAttacker, lostDefender + 1)
        else if (attackerRoll <= defenderRoll)
          (lostAttacker + 1, lostDefender)
        else
          (lostAttacker, lostDefender)
    }

    attackerState.removeTanks(wagonLostAttacker)
    defenderState.removeTanks(wagonLostDefender)
  }
  override def execute(attackManager: AttackManager): Unit =
    diceComparison(attackManager.diceRollAttacker,attackManager.diceRollDefender, attackManager.attacker, attackManager.defender)
}

class OutcomeAttackPhase(typeOfMap:VersionMap, gameMap:GameMap) extends AttackPhase{
  private def outcomeAttack(attacker: State, defender: State, typeOfMap: VersionMap, gameMap: GameMap): MessageAttackPhase =
    val NumberOfStateForWinClassicVersion = 24
    val NumberOfStateForWinEuropeanVersion = 13
    (attacker.numberOfTanks, defender.numberOfTanks, typeOfMap) match
      case (attackerTanks, 0, _) if attackerTanks > 1 =>
        defender.setPlayer(attacker.player)
        typeOfMap match
          case VersionMap.Classic if gameMap.playerStates(attacker.player).size >= NumberOfStateForWinClassicVersion => MessageAttackPhase.Winner
          case VersionMap.Classic if gameMap.playerStates(attacker.player).size < NumberOfStateForWinClassicVersion => MessageAttackPhase.ConqueredState
          case VersionMap.Europe if gameMap.playerStates(attacker.player).size >= NumberOfStateForWinEuropeanVersion => MessageAttackPhase.Winner
          case VersionMap.Europe if gameMap.playerStates(attacker.player).size < NumberOfStateForWinEuropeanVersion => MessageAttackPhase.ConqueredState
      case (attackerTanks, _, _) if attackerTanks == 1 => MessageAttackPhase.LoseAttack
      case _ => MessageAttackPhase.ContinueAttack
  override def execute(attackManager: AttackManager): Unit =
    attackManager.setResultMessage(outcomeAttack(attackManager.attacker, attackManager.defender, typeOfMap, gameMap))
}

class MovementInConquaredStatePhase extends AttackPhase{
  private def numberOfTanksToMove(attacker: State, numberOfDice:Int): Int = attacker match
    case stateAttacker if (stateAttacker.numberOfTanks - 1).equals(numberOfDice) => numberOfDice
    case _ => attacker.numberOfTanks
  override def execute(attackManager: AttackManager): Unit =
    attackManager.setNumberOfTanksToMove(numberOfTanksToMove(attackManager.attacker,attackManager.numberOfDice(attackManager.attacker,attackManager.defender)._1))
}


