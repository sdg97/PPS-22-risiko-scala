package utils

trait FSM:
  type Phase
  type Action
  def ::(p: Phase, a: Action) : FSM
  def trigger(a: Action): Unit
  def currentPhase: Phase

enum RisikoPhase:
  case StartTurn
  case Attack
  case Move

enum RisikoAction:
  case StartAttack
  case StartMove
  case AttackRequest
  case MoveRequest

abstract class FSMImpl() extends FSM:
  private val g = new GraphWithEdgeImpl with TraversableGraph:
    override type Node = Phase
    override type Edge = Action
  override def ::(p: Phase, a: Action): FSM =
    this
  override def trigger(a: Action): Unit = ???

  override def currentPhase: Phase = ???


//un qualsiasi oggetto iterabile a cui aggiungo

