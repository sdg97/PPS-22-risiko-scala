package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MovePhaseTest extends AnyFunSuite with Matchers:

  val map = new GameMap()
  val player1 = Player("pie", PlayerColor.YELLOW)

  val italy = State("italy", 8, player1,0,0)
  val france = State("france", 3, player1,0,0)

  map.addNode(italy)
  map.addNode(france)

  test("Test add wagon on my state"){
    map.moveWagon(italy.name, france.name,3)
    assert(map.stateByName("italy").numberOfWagon == 5)
    assert(map.stateByName("france").numberOfWagon == 6)
  }
