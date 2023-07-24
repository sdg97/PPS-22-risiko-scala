package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MovePhaseTest extends AnyFunSuite with Matchers:

  val map = new GameMap()
  val player1 = new PlayerImpl("pie", PlayerColor.YELLOW)

  val italy = new StateImpl("italy", 8, player1,0,0)
  val france = new StateImpl("france", 3, player1,0,0)

  map.addNode(italy)
  map.addNode(france)

  test("Test add wagon on my state"){
    map.shiftWagon(italy.name, france.name,3)
    assert(map.getStateByName("italy").numberOfWagon == 5)
    assert(map.getStateByName("france").numberOfWagon == 6)
  }
