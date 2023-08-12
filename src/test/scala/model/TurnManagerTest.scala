package model

import model.entity.Player
import model.manager.TurnManager
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

      assert(turnManager.next().username == "Simone")
      assert(turnManager.next().username == "Martin")
      assert(turnManager.next().username == "Pietro")
      assert(turnManager.next().username == "Simone")
    }
  }

