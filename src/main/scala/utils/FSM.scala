package utils

import utils.RisikoAction.{AttackRequest, EndTurn, MoveRequest, StartAttack, StartMove}
import utils.RisikoPhase.{Attack, StartTurn}

trait FSM:
  type Phase   // Node and Edge are abstract types, member of Graph
  type Action

  def + (p1: Phase, a: Action, p2: Phase): Unit

  def trigger(a: Action): Phase

  def currentPhase: Phase

  def next(): Iterable[Phase]

trait FSMImpl() extends FSM:
  private val g = new GraphWithEdgeImpl with TraversableGraph:
    override type Node = Phase
    override type Edge = Action

  override def +(p1: Phase, a: Action, p2: Phase): Unit =
    g.addEdge(p1,p2,a)

  override def trigger(a: Action): Phase =
    g.crossEdge(a)
    g.getCurrentNode().get

  override def currentPhase: Phase =
    g.getCurrentNode().get

  override def next(): Iterable[Phase] =
    g.getNeighbours(g.getCurrentNode().get)

enum RisikoPhase:
  case StartTurn;
  case Attack;
  case Move

enum RisikoAction:
  case StartMove
  case StartAttack
  case AttackRequest
  case MoveRequest
  case EndTurn

object RisikoFSM:
  def apply() =
    val f = new FSMImpl:
      override type Phase = RisikoPhase
      override type Action = RisikoAction
    f + (StartTurn, StartAttack, Attack)
    f + (Attack, AttackRequest, Attack)
    f + (Attack, StartMove, RisikoPhase.Move)
    f + (StartTurn, RisikoAction.StartMove, RisikoPhase.Move)
    f + (RisikoPhase.Move, MoveRequest, RisikoPhase.Move)
    f + (StartTurn, EndTurn, StartTurn)
    f + (RisikoPhase.Move, EndTurn, StartTurn)
    f + (Attack, EndTurn, StartTurn)

    f

object TryFSM extends App:

  import RisikoAction.*
  import RisikoPhase.*

  val f = RisikoFSM()

  println(f.currentPhase) //StartTurn
  println(f.trigger(StartMove)) //Move
  println(f.trigger(EndTurn)) //StartTurn

  println()

  println(f.currentPhase) //StartTurn
  println(f.trigger(StartAttack)) //Attack
  println(f.trigger(AttackRequest)) //Attack
  println(f.trigger(AttackRequest)) //Attack
  println(f.trigger(StartMove)) //Move
  println(f.trigger(MoveRequest)) //Move
  println(f.trigger(MoveRequest)) //Move
  println(f.trigger(EndTurn)) //StartTurn

  println(f.next()) //Move Attack StartTurn
  f.trigger(StartAttack)
  println(f.next()) //Attack Move StartTurn
  f.trigger(StartMove)
  println(f.next()) //Move StartTurn

