package model.manager

import model.manager.{RisikoAction, RisikoPhase, RisikoRequest, RisikoSwitchPhaseAction}
import utils.FSMImpl

/**
 * Risiko turn phase
 */
enum RisikoPhase:
  case StartTurn;
  case Attack;
  case Move

/**
 * Risiko game action
 */
trait RisikoAction

/**
 * Risiko action that trigger a turn phase switch
 */
enum RisikoSwitchPhaseAction extends RisikoAction:
  case StartMove
  case StartAttack
  case EndTurn

/**
 * Risiko action that represent a user intention to do something
 */
enum RisikoRequest extends RisikoAction:
  case AttackRequest
  case MoveRequest

object RisikoSwitchPhaseAction:
  extension (a: RisikoSwitchPhaseAction)

    /**
     * get a phase useful description
     */
    def string : String = a match
      case StartMove => "Move Phase"
      case StartAttack => "Attack Phase"
      case EndTurn => "Pass"


/**
 * Turn phase manager factory
 */
object TurnPhasesManager:

  import RisikoPhase.*
  import RisikoRequest.*
  import RisikoSwitchPhaseAction.*

  /**
   * @return a new turn manger for Risiko
   */
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


