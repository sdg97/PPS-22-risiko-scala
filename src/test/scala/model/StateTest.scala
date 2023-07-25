package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class StateTest extends AnyFunSuite with Matchers:
  val player = Player("player1", PlayerColor.YELLOW)
  val state = State("Brasile", 10, player,0,0)

  test("Test create State"){
    assert(state.name=="Brasile")
    assert(state.numberOfWagon.equals(10))
  }

  test("Test add wagon to State"){
    state.addWagon(2)
    assert(state.numberOfWagon.equals(12))
  }

  test("Test remove wagon to State"){
    state.removeWagon(2)
    assert(state.numberOfWagon.equals(10))
  }
