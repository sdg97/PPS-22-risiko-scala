package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class StateTest extends AnyFunSuite with Matchers:
  val player = Player("player1", PlayerColor.YELLOW)
  val state = State("Brasile", 10, player,0,0)

  test("Test create State"){
    assert(state.name=="Brasile")
    assert(state.numberOfTanks.equals(10))
  }

  test("Test add wagon to State"){
    state.addTanks(2)
    assert(state.numberOfTanks.equals(12))
  }

  test("Test remove wagon to State"){
    state.removeTanks(2)
    assert(state.numberOfTanks.equals(10))
  }
