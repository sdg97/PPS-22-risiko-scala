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
  given AssignExtension[Seq[State], Int] with
      extension (accountables: Seq[State]) def assign(assignable: Int): Seq[State] =
        val assigment: Map[State, Int] = Assign.assign(accountables, assignable)
        println(assigment)
        accountables.foreach{s => s.addWagon(assigment(s))}
        accountables

object M2 extends App:
  import AssignExtensionGivenInstances.given

  val ac3 : Seq[State] = State("brasile", 0, Player("simone", BLUE), 0, 0)
    :: State("polonia", 0, Player("simone", BLUE), 0, 0)
    :: State("russia", 0, Player("simone", BLUE), 0, 0)
    :: Nil

  ac3 assign 12
  ac3.foreach{s => println(s.numberOfWagon) }


