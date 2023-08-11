package model

import model.entity.map.State
import model.entity.{Player, PlayerColor}
import org.junit.Test
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class StateTest:

  val player1: Player = Player("pietro", PlayerColor.YELLOW)
  val player2: Player = Player("luca", PlayerColor.BLUE)
  val state: State = State("italy", 5, player1)

  @Test
  def testCreateState(): Unit =
    assert(state.name == "italy")
    assert(state.numberOfTanks.equals(5))
    assert(state.player == player1)

  @Test
  def testAddTanksToState(): Unit =
    state.addTanks(2)
    assert(state.numberOfTanks.equals(7))

  @Test
  def testRemoveTanksToState(): Unit =
    state.removeTanks(2)
    assert(state.numberOfTanks.equals(3))

  @Test
  def testAssignStateToPlayer(): Unit =
    state.setPlayer(player2)
    assert(state.player == player2)