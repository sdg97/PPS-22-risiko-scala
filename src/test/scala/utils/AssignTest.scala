package utils

import model.PlayerColor.BLACK
import model.{Player, State}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class AssignTest extends AnyFunSpec with Matchers:
  import AssignGivenInstances.given

  describe("An assignment ") {
    describe(" of a States Set to a Players Set") {
      it(" should be fair") {
        val states: Set[State] = statesFromFile("src/main/resources/config/states.txt")
        val players = Set(Player("simone"),
          Player("pietro"),
          Player("martin"),
          Player("mirko"),
          Player("gianluca")
        )
        players assign states
        val sizes = states.groupBy(_.player.username)
          .map(_._2.size)
        val max = sizes.max
        sizes.foreach(s =>{
          assert(Math.abs(s - max) <= 1)
        })
      }
    }
    describe(" of Troops to States in a random way") {
      it(" should be fair") {
        val states: Set[State] = statesFromFile("src/main/resources/config/states.txt")
        val players = Set(Player("simone"),
          Player("pietro"),
          Player("martin"),
          Player("mirko"),
          Player("gianluca")
        )
        players assign states
        players.foreach(p => {
          val playersStates = states.filter(s => s.player.username == p.username)
          playersStates assign players.START_TANK_NUMBER
          val sizes = playersStates.map(_.numberOfTanks)
          val max = sizes.max
          sizes.foreach(s => {
            assert(Math.abs(s - max) <= 1)
          })
        })
      }
    }
  }