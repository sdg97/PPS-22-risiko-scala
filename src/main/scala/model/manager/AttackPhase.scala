package model.manager

import model.entity.map.{GameMap, State}

import scala.util.Random


trait AttackPhase {
  /**
   * method that execute a specify attack's phase
   * @param attackManager the instance of manager about attack process
   */
  def execute(attackManager: AttackManager): Unit

}

/**
 * class that hold the rolling dice attack phase
 */
class RollingDicePhase extends AttackPhase{

  /**
   * @return the result of attacker/defender roll dice
   * @param typeOfPlayer specify if the player is attacker or defender
   * @param numberDice the number of player's dice
   */
  private def rollDicePhase(typeOfPlayer: String, numberDice:Int): DiceRoll = typeOfPlayer match
    case "attacker" => Seq.fill(numberDice)(Random.nextInt(6) + 1).sorted.reverse
    case "defender" => Seq.fill(numberDice)(Random.nextInt(6) + 1).sorted.reverse
    case _ => null

  override def execute(attackManager: AttackManager): Unit =
    attackManager.setDiceRollAttacker(rollDicePhase("attacker",attackManager.numberOfDiceAttacker()))
    attackManager.setDiceRollDefender(rollDicePhase("defender",attackManager.numberOfDiceDefender()))
}

/**
 * class that hold the dice comparison phase
 */
class DiceComparisonPhase extends AttackPhase{

  /**
   * method that compare the attacker/defender roll dice,
   * calculate the tank losses by the player
   * remove the losses tank to the players
   * @param typeOfPlayer specify if the player is attacker or defender
   * @param numberDice   the number of player's dice
   */
  private def diceComparison(attackManager: AttackManager): Unit = {
    val (wagonLostAttacker, wagonLostDefender) = attackManager.diceRollAttacker.sorted.reverse.zip(attackManager.diceRollDefender.sorted.reverse).foldLeft((0, 0)) {
      case ((lostAttacker, lostDefender), (attackerRoll, defenderRoll)) =>
        if (attackerRoll > defenderRoll)
          (lostAttacker, lostDefender + 1)
        else if (attackerRoll <= defenderRoll)
          (lostAttacker + 1, lostDefender)
        else
          (lostAttacker, lostDefender)
    }

    attackManager.attacker.removeTanks(wagonLostAttacker)
    attackManager.defender.removeTanks(wagonLostDefender)
  }
  override def execute(attackManager: AttackManager): Unit =
    diceComparison(attackManager: AttackManager)
}

/**
 * class that hold the outcome attack phase
 */
class OutcomeAttackPhase(typeOfMap:VersionMap, gameMap:GameMap) extends AttackPhase{
  /**
   * method that verify the number of attacker's/defender's tanks and
   * verify if the attacker conquered the defender state and
   * in base of the typology of map selected verify if the attacker win the game.
   *
   * @return the outcome of attack
   * @param attackManager instance of attack's phase manager
   * @param typeOfMap     the version of map selected by the user's
   * @param gameMap       the instance of the game map
   */
  private def outcomeAttack(attackManager: AttackManager, typeOfMap: VersionMap, gameMap: GameMap): MessageAttackPhase =
    val NumberOfStateForWinClassicVersion = 24
    val NumberOfStateForWinEuropeanVersion = 13
    (attackManager.attacker.numberOfTanks, attackManager.defender.numberOfTanks, typeOfMap) match
      case (attackerTanks, 0, _) if attackerTanks > 1 =>
        attackManager.defender.setPlayer(attackManager.attacker.player)
        typeOfMap match
          case VersionMap.Classic if gameMap.playerStates(attackManager.attacker.player).size >= NumberOfStateForWinClassicVersion => MessageAttackPhase.Winner
          case VersionMap.Classic if gameMap.playerStates(attackManager.attacker.player).size < NumberOfStateForWinClassicVersion => MessageAttackPhase.ConqueredState
          case VersionMap.Europe if gameMap.playerStates(attackManager.attacker.player).size >= NumberOfStateForWinEuropeanVersion => MessageAttackPhase.Winner
          case VersionMap.Europe if gameMap.playerStates(attackManager.attacker.player).size < NumberOfStateForWinEuropeanVersion => MessageAttackPhase.ConqueredState
      case (attackerTanks, _, _) if attackerTanks == 1 => MessageAttackPhase.LoseAttack
      case _ => MessageAttackPhase.ContinueAttack
  override def execute(attackManager: AttackManager): Unit =
    attackManager.setResultMessage(outcomeAttack(attackManager, typeOfMap, gameMap))
}

/**
 * class that hold the tank movement phase after conquered state
 */
class TankMovementPhase extends AttackPhase{
  /**
   * method that verify, in base of the number of attacker's dice
   * and the number of attacker's tanks,
   * the number of tank to movement in the conquered state.
   *
   * @return the tanks to movement in conquered state
   * @param attackManager instance of attack's phase manager
   */
  private def tankMovement(attackManager: AttackManager): Int =
    val diceAttacker=attackManager.numberOfDiceAttacker()
    attackManager.attacker match
    case stateAttacker if (stateAttacker.numberOfTanks - 1).equals(diceAttacker) => diceAttacker
    case _ => attackManager.attacker.numberOfTanks
  override def execute(attackManager: AttackManager): Unit =
    attackManager.setNumberOfTanksToMove(tankMovement(attackManager))
}


