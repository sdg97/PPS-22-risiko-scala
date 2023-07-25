package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameMapTest extends AnyFunSuite with Matchers:

  val map = new GameMap()
  val player1 = Player("pie", PlayerColor.YELLOW)
  val player2 = Player("martin", PlayerColor.BLUE)
  val player3 = Player("simo", PlayerColor.BLUE)

  val italy = State("italy", 3, player1,0,0)
  val france = State("france", 3, player2,0,0)
  val brazil = State("brazil", 5, player1,0,0)

  test("Test add Node"){
    map.addNode(italy)
    assert(map.stateByName("italy").equals(italy))
  }

  test("Test neighbour"){
    map.addNode(italy)
    map.addNode(france)
    map.addEdge("italy", "france")
    assert(map.neighborStates("italy", player1).contains("france"))
  }

  test("Test player's states"){
    map.addNode(italy)
    map.addNode(brazil)
    assert(map.playerStates(player1).contains(italy))
    assert(map.playerStates(player1).contains(brazil))
  }
