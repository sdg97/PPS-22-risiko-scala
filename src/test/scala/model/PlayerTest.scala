package model
import model.entity.Player
import model.entity.PlayerColor.BLACK
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlayerTest extends AnyFunSuite with Matchers:
  val player= Player("Martin", BLACK)
  test("Test create Player"){
    assert(player.getUsername.equals("Martin"))
    assert(player.getColor.equals(BLACK))
  }
