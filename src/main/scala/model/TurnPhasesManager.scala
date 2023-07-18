package model

import utils.FSMImpl

enum RisikoPhase:
  case StartTurn;
  case Attack;
  case Move

trait RisikoAction
enum RisikoSwitchPhaseAction extends RisikoAction:
  case StartMove
  case StartAttack
  case EndTurn

enum RisikoRequest extends RisikoAction:
  case AttackRequest
  case MoveRequest

object TurnPhasesManager:

  import RisikoPhase.*
  import RisikoRequest.*
  import RisikoSwitchPhaseAction.*
  def apply() =
    val f = new FSMImpl:
      override type Phase = RisikoPhase
      override type Action = RisikoAction
    f + (StartTurn, StartAttack, Attack)
    f + (Attack, AttackRequest, Attack)
    f + (Attack, StartMove, Move)
    f + (StartTurn, StartMove, Move)
    f + (StartTurn, EndTurn, StartTurn)
    f + (Move, MoveRequest, Move)
    f + (Move, EndTurn, StartTurn)
    f + (Attack, EndTurn, StartTurn)
    f

object TryFSM extends App:

  import RisikoPhase.*
  import RisikoRequest.*
  import RisikoSwitchPhaseAction.*

  val f = TurnPhasesManager()

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

  println(f.next) //Move Attack StartTurn
  f.trigger(StartAttack)
  println(f.next) //Attack Move StartTurn
  f.trigger(StartMove)
  println(f.next) //Move StartTurn
  f.trigger(EndTurn)

  println("Available actions")

  println(f.permittedAction) //StartMove StartAttack EndTurn
  f.trigger(StartAttack)
  println(f.permittedAction) //StartAttack EndTurn
  f.trigger(StartMove)
  println(f.permittedAction) //EndTurn


