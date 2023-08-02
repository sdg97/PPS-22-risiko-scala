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

object RisikoSwitchPhaseAction:
  extension (a: RisikoSwitchPhaseAction)
    def string : String = a match
      case StartMove => "Move Phase"
      case StartAttack => "Attack Phase"
      case EndTurn => "Pass"


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
    f + (Move, MoveRequest, StartTurn)
    f + (Move, EndTurn, StartTurn)
    f + (Attack, EndTurn, StartTurn)
    f


