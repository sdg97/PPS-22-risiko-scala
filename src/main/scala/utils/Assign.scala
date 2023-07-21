package utils

import model.PlayerColor.{BLACK, BLUE, GREEN}
import model.{Player, State}

import scala.collection.mutable.{HashMap, Map}
import scala.collection.immutable.{Nil, Seq}
import scala.util.Random

type Troop = Int

trait Assign[A, B, C]:
  def assign(accountables: A, assignable: B): C
object Assign:
  def assign[A, B, C](accountables: A, assignable: B)(using a: Assign[A, B, C]) =
    a.assign(accountables, assignable)

object AssignableGivenInstances:

  import Assign.*

  private def fairAssign[X, Y](accountables: Seq[X], toAssign: Seq[Y]): Seq[(X, Seq[Y])] =
    def getRandomElement[E](seq: Seq[E]): E =
      val random = new Random()
      seq(random.nextInt(seq.length))

    def f(accountables: Seq[X], assignable: Seq[Y], i: Int): Seq[(X, Y)] =
      assignable.length match
        case 0 => Nil
        case _ =>
          val ac = accountables(i)
          val as : Y = getRandomElement(assignable)
          val index = if i + 1 < accountables.length then i + 1 else 0
          f(accountables, assignable.filter(y => as != y), index).+:((ac, as))

    f(accountables, toAssign, 0).groupBy(_._1)
      .toSeq.map { x => (x._1, x._2.map {_._2})}

  private def fairAssign[X](accountables: Seq[X], assignable: Int ): Map[X, Int] =
    def f(accountables: Seq[X], assignable: Int, i: Int, m: Map[X, Int]): Map[X, Int] =
      assignable match
        case 0 => m
        case _ =>
          val ac = accountables(i)
          val index = if i + 1 < accountables.length then i + 1 else 0
          val v : Int  =  if m.keySet.contains(ac) then  m(ac) + 1 else 1
          m += (ac -> v)
          f(accountables, assignable - 1 , index, m)

    f(accountables, assignable, 0, new HashMap[X,Int]())

  private class SeqAssign[X,Y] extends Assign[Seq[X], Seq[Y], Seq[(X, Seq[Y])]]:
    override def assign(accountables: Seq[X], assignable: Seq[Y]): Seq[(X, Seq[Y])] = fairAssign(accountables, assignable)

  private class SeqIntAssign[X] extends Assign[Seq[X], Int, Map[X, Int]]:
    override def assign(accountables: Seq[X], assignable: Int): Map[X, Int] = fairAssign(accountables, assignable)

  given seqAssignPlayerState : Assign[Seq[Player], Seq[State], Seq[(Player,Seq[State])]] = new SeqAssign[Player, State]()
  given assignPlayerTroop : Assign[Seq[Player], Troop, Map[Player,Troop]] = new SeqIntAssign[Player]()
  given assignStateTroop: Assign[Seq[State], Troop, Map[State, Troop]] = new SeqIntAssign[State]()
object M extends App:
  import Assign.*
  import AssignableGivenInstances.given

  val ac : Seq[String] = "simone" :: "martin" :: "pietro" :: Nil
  val as: Seq[String] = "italia" :: "francia" :: "germania" :: "fillandia" :: "inghilterra" :: "brasile" :: "messico" :: "congo" :: Nil

  // println(assign(ac, as))

  val t :Troop = 50
  val ac2 : Seq[Player]= Player("simone", BLUE)
    :: Player("martin", BLACK)
    :: Player("pietro", GREEN) :: Nil

  println(assign(ac2,t))

  val ac3 : Seq[State] = State("brasile", 0, Player("simone", BLUE), 0, 0)
    :: State("polonia", 0, Player("simone", BLUE), 0, 0)
    :: State("russia", 0, Player("simone", BLUE), 0, 0)
    :: Nil

  println(assign(ac3, 17))

