package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlacementPhaseTest extends AnyFunSuite with Matchers:

  val map = new GameMap()
  val player1 = new PlayerImpl("pie", PlayerColor.YELLOW)
  val player2 = new PlayerImpl("martin", PlayerColor.BLUE)

  val italy = new StateImpl("italy", 3, player1,0,0)
  val france = new StateImpl("france", 3, player2,0,0)

  map.addNode(italy)
  map.addNode(france)

  test("Test add wagon on my state"){
    italy.addWagon(1)
    assert(map.stateByName("italy").numberOfWagon == 4)
  }
