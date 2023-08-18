package model.manager.attackmanager

import model.entity.map.{GameMap, State}
import model.entity.{Player, PlayerColor}
import model.manager.{GameSettingManager, VersionMap}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class AttackPhaseTest extends AnyFunSpec with Matchers:
  val gameMap = new GameMap()
  val attackManager = AttackManager(gameMap)
  attackManager.setAttacker(State("Peru", 4, Player("Martin", PlayerColor.RED), (20, 50)))
  attackManager.setDefender(State("Argentina", 2, Player("Pietro", PlayerColor.YELLOW), (50, 90)))

  describe("RollingDiceAttackPhase"){
    it("should roll dice correctly for attacker and defender"){
      assert(attackManager.rollDiceAttacker == (null))
      assert(attackManager.rollDiceDefender == (null))
      val rollingDicePhase= new RollingDicePhase
      rollingDicePhase.execute(attackManager)
      assert(attackManager.rollDiceAttacker.size.equals(3))
      assert(attackManager.rollDiceDefender.size.equals(2))
    }
  }

  describe("DiceComparisonPhase"){
    it("should perform comparison of dice correctly"){
      attackManager.setRollDiceAttacker(Seq(6,6,6))
      attackManager.setRollDiceDefender(Seq(1,1))
      val diceComparisonPhase=new DiceComparisonPhase
      diceComparisonPhase.execute(attackManager)
      assert(attackManager.attacker.numberOfTanks.equals(4))
      assert(attackManager.defender.numberOfTanks.equals(0))
    }
  }

  describe("OutcomeAttackPhase"){
    it("should determine the outcome of attack correctly"){
      val outcomeAttackPhase= new OutcomeAttackPhase(VersionMap.Classic, gameMap)
      outcomeAttackPhase.execute(attackManager)
      assert(attackManager.resultMessage.equals(MessageAttackPhase.ConqueredState))
    }
  }

  describe("TankMovementPhase"){
    it("should determine the number of tank to move in the conquered state correctly"){
      val tankMovementPhase=new TankMovementPhase
      tankMovementPhase.execute(attackManager)
      assert(attackManager.numberOfTanksToMove().equals(Seq(3)))
    }
  }


