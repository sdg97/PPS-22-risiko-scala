package model

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random
class TurnManagerTest extends AnyFunSpec with Matchers:
  describe(" Turn Manager "){
    it(" should have a circular way to get the next ") {
      val turnManager: TurnManager[Player] = TurnManager(Set(
        Player("Simone"),
        Player("Martin"),
        Player("Pietro")))

      assert(turnManager.next().getUsername == "Simone")
      assert(turnManager.next().getUsername == "Martin")
      assert(turnManager.next().getUsername == "Pietro")
      assert(turnManager.next().getUsername == "Simone")
    }
  }

