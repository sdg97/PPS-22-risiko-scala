package model

import model.entity.{Goal, Player}
import model.entity.map.State
import model.manager.{MessageAttackPhase, RisikoAction, RisikoPhase, RisikoSwitchPhaseAction, SettingResult, VersionMap}

trait Model {

  def setGameSettings(inputDataPlayer: List[(String, String)], typeOfMap: String): SettingResult

  def players: List[Player]

  def deployTroops(): Unit

  def getAttacker(): State

  def getDefender(): State

  /**
   * @param stateName the state's name from which to search for neighbors
   * @return a Set of the names of neighboring states with the state passed as a parameter and owned by enemy players
   */
  def neighborStatesOfEnemies(stateName: String): Set[String]

  /**
   * @param stateName the state's name from which to search for neighbors
   * @return a Set of the names of neighboring states with the state passed as a parameter and owned by the player passed as a parameter
   */
  def neighborStatesOfPlayer(stateName: String): Set[String]

  /**
   *
   * @return a Set of all states owned by the current player
   */
  def currentPlayerStates: Set[State]

  /**
   *
   * @return a Set of all the states
   */
  def allStates: Set[State]

  def attack(): Unit

  def attackResult(): MessageAttackPhase

  def rollDiceAttacker(): Seq[Int]

  def rollDiceDefender(): Seq[Int]

  def setAttacker(state: State): Unit

  def setDefender(state: State): Unit

  def numberOfDiceForPlayers(attacker: State, defender: State): (Int, Int)

  def numberOfTanksToMove(attacker: State): Int

  /**
   *
   * @return the current player
   */
  def currentPlayer: Player

  /**
   * @param stateName the name of the state
   * @return the state object
   */
  def stateByName(stateName: String): State

  /**
   * @param stateName the name of the state to assign the tanks to
   * @return a Set of all states owned by the current player
   */
  def addTank(stateName: String): Unit

  /**
   * @return the number of tanks that the current player can place.
   */
  def tanksToPlace: Int

  /**
   * @return the actions available to change phase of the turn
   */
  def switchTurnPhaseActionAvailable: Set[RisikoAction]

  /**
   * Switch phase turn
   * @param action to switch turn phase
   */
  def switchPhase(a: RisikoSwitchPhaseAction): Unit

  /**
   *
   * @return the current turn phase
   */
  def currentPhase: RisikoPhase

  /**
   *
   * moves tanks from a state to another state
   *
   * @param fromStateName starting state
   * @param toStateName   arrival status
   * @param numberOfTanks the number of tanks to move
   */
  def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit

  def setDefaultAttackSettings: Unit

  def setDefaultInitialSettings(): Unit

  def setTypeOfMap(): VersionMap

  /**
   * @return the goal for win the game
   */
  def goal: Goal

}