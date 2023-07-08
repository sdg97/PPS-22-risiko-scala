package model
import model.PlayerColor.Black
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PlayerTest extends AnyFunSuite with Matchers:
  val player=new PlayerImpl("Martin", Black)
  test("Test create Player"){
    assert(player.username.equals("Martin"))
    assert(player.color.equals(Black))
  }
