package utils

import model.{Player, State}

import scala.util.Random

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
          println(accountables)
          println("sono qui " + i.toString + accountables(i))
          val ac = accountables(i)
          val as : Y = getRandomElement(assignable)
          val index = if i + 1 < accountables.length then i + 1 else 0
          f(accountables, assignable.filter(y => as != y), index).+:((ac, as))

    f(accountables, toAssign, 0).groupBy(_._1)
      .toSeq.map { x => (x._1, x._2.map {_._2})}

  private class SeqAssign[X,Y] extends Assign[Seq[X], Seq[Y], Seq[(X, Seq[Y])]]:
    override def assign(accountables: Seq[X], assignable: Seq[Y]): Seq[(X, Seq[Y])] = fairAssign(accountables, assignable)

  given seqAssignString : Assign[Seq[String], Seq[String], Seq[(String,Seq[String])]] = new SeqAssign[String, String]()
  given seqAssignPlayerState : Assign[Seq[Player], Seq[State], Seq[(Player,Seq[State])]] = new SeqAssign[Player, State]()
object M extends App:
  import Assign.*
  import AssignableGivenInstances.seqAssignString

  val ac : Seq[String] = "simone" :: "martin" :: "pietro" :: Nil
  val as: Seq[String] = "italia" :: "francia" :: "germania" :: "fillandia" :: "inghilterra" :: "brasile" :: "messico" :: "congo" :: Nil


  println(assign(ac, as))
