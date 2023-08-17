package utils
import model.entity.PlayerColor.{BLACK, BLUE, GREEN}
import model.entity.Player
import model.entity.map.State

import scala.collection.mutable.{HashMap, Map}
import scala.collection.immutable.{Nil, Seq}
import scala.util.Random

/**
 * Represent the assigment of resource B to A
 * @tparam A: who get the resource
 * @tparam B: the resource
 */
trait Assign[A, B]:

  /**
   * extend A behavior for get the assigment of B resource
   */
  extension (accountable: A) def assign(assignable: B): A

/**
 * Assigment of resource to some entity that get them
 */
object AssignGivenInstances:
  private def fairAssign[X, Y](accountables: Seq[X], toAssign: Seq[Y]): Seq[(X, Seq[Y])] =
    def getRandomElement[E](seq: Seq[E]): E =
      val random = new Random()
      seq(random.nextInt(seq.length))

    def f(accountables: Seq[X], assignable: Seq[Y], i: Int): Seq[(X, Y)] =
      assignable.length match
        case 0 => Nil
        case _ =>
          val ac = accountables(i)
          val as: Y = getRandomElement(assignable)
          val index = if i + 1 < accountables.length then i + 1 else 0
          f(accountables, assignable.filter(y => as != y), index).+:((ac, as))

    f(accountables, toAssign, 0).groupBy(_._1)
      .toSeq.map { x => (x._1, x._2.map {
      _._2
    })
    }

  private def fairAssign[X](accountables: Seq[X], assignable: Int): Map[X, Int] =
    def f(accountables: Seq[X], assignable: Int, i: Int, m: Map[X, Int]): Map[X, Int] =
      assignable match
        case 0 => m
        case _ =>
          val ac = accountables(i)
          val index = if i + 1 < accountables.length then i + 1 else 0
          val v: Int = if m.keySet.contains(ac) then m(ac) + 1 else 1
          m += (ac -> v)
          f(accountables, assignable - 1, index, m)

    f(accountables, assignable, 0, new HashMap[X, Int]())

  /**
   * Assign a number to a States Set with a fair policy
   */
  given Assign[Set[State], Int] with
      extension (accountables: Set[State]) def assign(assignable: Int): Set[State] =
        val assigment: Map[State, Int] = fairAssign(accountables.toSeq, assignable)
        accountables.foreach{s => s.addTanks(assigment(s))}
        accountables

  /**
   * Assign a States Set to a list of Players
   */
  given Assign[List[Player], Set[State]] with
    extension (accountables: List[Player]) def assign(assignable: Set[State]): List[Player] =
      fairAssign(accountables.toSeq, assignable.toSeq).foreach { t => t._2.foreach { s => s.setPlayer(t._1) } }
      accountables



