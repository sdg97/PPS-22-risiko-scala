package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameMapTest extends AnyFunSuite with Matchers:

  val map = new GameMap()
  val player1 = new PlayerImpl("pie", PlayerColor.Yellow)
  val player2 = new PlayerImpl("martin", PlayerColor.Blue)
  val player3 = new PlayerImpl("simo", PlayerColor.Blue)

  val italy = new StateImpl("italy", 3, player1)
  val france = new StateImpl("france", 3, player2)
  val brazil = new StateImpl("brazil", 5, player1)

  test("Test add Node"){
    map.addNode(italy)
    assert(map.getStateByName("italy").equals(italy))
  }

  test("Test neighbour"){
    map.addNode(italy)
    map.addNode(france)
    map.addEdge("italy", "france")
    assert(map.getNeighborStates("italy", player1).contains("france"))
  }

  test("Test player's states"){
    map.addNode(italy)
    map.addNode(brazil)
    assert(map.getPlayerStates(player1).contains(italy))
    assert(map.getPlayerStates(player1).contains(brazil))
  }
