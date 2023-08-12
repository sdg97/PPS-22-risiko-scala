package model

import model.entity.{Player, PlayerColor}
import model.entity.map.{GameMap, State}
import model.manager.{AttackManager, MessageAttackPhase, VersionMap}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class AttackManagerTest extends AnyFunSpec with Matchers:
  describe("Attack Manager"){
    val gameMap=new GameMap()
    val attackManager=AttackManager(gameMap)
    val attacker=State("Peru", 4, Player("Martin", PlayerColor.RED), (20, 50))
    val defender= State("Argentina", 2, Player("Pietro", PlayerColor.YELLOW), (50, 90))
    attackManager.setAttacker(attacker)
    attackManager.setDefender(defender)

    attackManager.attackPhase(VersionMap.Classic)

    it("should have a way to get the number of Attacker's dice"){
      assert(attackManager.numberOfDice(State("Peru", 4, Player("Martin", PlayerColor.RED), (20, 50)),State("Argentina", 2, Player("Pietro", PlayerColor.YELLOW), (50, 90)))._1.equals(3))
    }

    it("should have a way to get the number of Defender's dice") {
      assert(attackManager.numberOfDice(State("Peru", 4, Player("Martin", PlayerColor.RED), (20, 50)), State("Argentina", 2, Player("Pietro", PlayerColor.YELLOW), (50, 90)))._2.equals(2))
    }

    it("should have a way to get the result of Attacker's roll dice") {
      assert(attackManager.rollDiceAttacker.size.equals(3))
    }

    it("should have a way to get the result of Defender's roll dice") {
      assert(attackManager.rollDiceDefender.size.equals(2))
    }

    it("should have a way to get the result of attack") {
      assert(attackManager.resultMessage.equals(MessageAttackPhase.ConqueredState) ||
        attackManager.resultMessage.equals(MessageAttackPhase.LoseAttack) ||
        attackManager.resultMessage.equals(MessageAttackPhase.ContinueAttack))
    }

    it("should have a way to get the number of tank to move in a conquered state"){
      assert(attackManager.numberOfTanksToMove(State("Peru", 4, Player("Martin", PlayerColor.RED), (20, 50))).equals(3))
    }

    it("should have a way to set default attack settings"){
      attackManager.setDefaultSettings
      assert(attackManager.attacker == (null))
      assert(attackManager.defender == (null))
      assert(attackManager.rollDiceAttacker == (null))
      assert(attackManager.rollDiceDefender == (null))
      assert(attackManager.resultMessage == null)
    }

  }
