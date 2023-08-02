package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlacementPhaseTest extends AnyFunSuite with Matchers:

  val map = new GameMap()
  val player1 = Player("pie", PlayerColor.YELLOW)
  val player2 = Player("martin", PlayerColor.BLUE)

  val italy = State("italy", 3, player1,0,0)
  val france = State("france", 3, player2,0,0)

  map.addNode(italy)
  map.addNode(france)

  test("Test add wagon on my state"){
    italy.addTanks(1)
    assert(map.stateByName("italy").numberOfTanks == 4)
  }
