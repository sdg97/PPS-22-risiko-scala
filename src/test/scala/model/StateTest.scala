package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class StateTest extends AnyFunSuite with Matchers:
  val player = new PlayerImpl("player1", PlayerColor.YELLOW)
  val state = new StateImpl("Brasile", 10, player)

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

class StateAllocation extends AnyFunSuite with Matchers:
  trait Assignable[A,B,C]:
    def assign(accountables: A, toAssign: B): C

  object Assignable:
    def assign[X,Y](accountables: Seq[X], toAssign: Seq[Y])(using assignable: Assignable[Seq[X],Seq[Y], Seq[(X,Seq[Y])]]): Seq[(X,Seq[Y])] =
      assignable.assign(accountables, toAssign)

  object AssignableGivenInstances:
    import Assignable.*
    given Assignable[Seq[Player], Seq[State], Seq[(Player, Seq[State])]] with
      override def assign(accountables: Seq[Player], toAssign: Seq[State]) :Seq[(Player, Seq[State])]  = ???

  test("Allocation with different strategies"){
    def getRandomElement[E](seq: Seq[E]): E =
      val random = new Random()
      seq(random.nextInt(seq.length))

    def getRandomElements[E](seq:Seq[E], howMuch: Int): Seq[E] = howMuch match
      case 0 => Nil
      case _ =>
        val current = getRandomElement(seq)
        getRandomElements(seq.filter{ _ != current }, howMuch -1).a


    def assign(accountables: Seq[Player], toAssign: Seq[State]): Seq[(Player, Seq[State])] =
      accountables.length match
        case 0 => Nil
        case 1 =>
          val ac = accountables(0)
          val as = getRandomElements(toAssign, toAssign.length)
          f(accountables.filter(x => x != ac), toAssign.filter(y => !as.contains(y))) + (ac, as)
        case _ =>
          val ac = accountables(0)
          val as = getRandomElements(toAssign, accountables.length / toAssign.length)
          f(accountables.filter(x => x != ac), toAssign.filter(y => !as.contains(y))) + (ac, as)

    import PlayerColor.*
    val players = Player("ptr", BLACK) :: Player("mrt", BLUE) :: Player("sdg", YELLOW) :: Nil
    val states = State("S", 0, null) ::  State("S", 0, null) ::  State("S", 0, null) :: Nil
    println(assign(players,states))
  }

  test("Allocation with a default strategy"){

  }
