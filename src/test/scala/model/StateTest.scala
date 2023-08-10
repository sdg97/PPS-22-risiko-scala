package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class StateTest extends AnyFunSuite with Matchers:
  val player1: Player = Player("pietro", PlayerColor.YELLOW)
  val player2: Player = Player("luca", PlayerColor.BLUE)

  val state: State = State("italy", 5, player1)

  test("Test create State"){
    assert(state.name == "italy")
    assert(state.numberOfTanks.equals(5))
    assert(state.player == player1)
  }

  test("Test add wagon to State"){
    state.addTanks(2)
    assert(state.numberOfTanks.equals(7))
  }

  test("Test remove wagon to State"){
    state.removeTanks(2)
    assert(state.numberOfTanks.equals(3))
  }

  test("Test assign to player") {
    state.setPlayer(player2)
    assert(state.player == player2)
  }
