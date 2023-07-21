package utils
import model.PlayerColor.{BLACK, BLUE, GREEN}
import model.{Player, State}

import scala.collection.mutable.{HashMap, Map}
import scala.collection.immutable.{Nil, Seq}
import scala.util.Random

trait AssignExtension[A, B]:
  extension (accountable: A) def assign(assignable: B): A

object AssignExtensionGivenInstances:
  import Assign.*
  import AssignableGivenInstances.given
  given AssignExtension[Set[State], Int] with
      extension (accountables: Set[State]) def assign(assignable: Int): Set[State] =
        val assigment: Map[State, Int] = Assign.assign(accountables.toSeq, assignable)
        println(assigment)
        accountables.foreach{s => s.addWagon(assigment(s))}
        accountables

  given AssignExtension[Set[Player], Set[State]] with
    extension (accountables: Set[Player]) def assign(assignable: Set[State]): Set[Player] =
      val players: Seq[Player] = accountables.toSeq
      val states: Seq[State] = assignable.toSeq
      Assign.assign(players, states).foreach { t => t._2.foreach { s => s.setPlayer(t._1) } }
      accountables



