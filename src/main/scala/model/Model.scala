package model

import model.entity.{Goal, Player}
import model.entity.map.State
import model.manager.attackmanager.MessageAttackPhase
import model.manager.{RisikoAction, RisikoPhase, RisikoSwitchPhaseAction, SettingResult, VersionMap}

trait Model {

  /**
   * method that initializes the game in case of correct insert of input data by players
   * @return the result of the check about the input data inserted by players
   * @param inputDataPlayer the players’s initial settings
   * @param typeOfMap       the type of map selected
   */
  def setGameSettings(inputDataPlayer: List[(String, String)], typeOfMap: String): SettingResult

  /**
   * @return a list about all players
   */
  def players: List[Player]

  /**
   * @return the state from which starts the attack.
   */
  def attackerState(): State

  /**
   * @return the attacked state.
   */
  def defenderState(): State

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

  /**
   * method that organize and execute the attack process.
   */
  def executeAttack(): Unit

  /**
   *
   * @return the message about different scenarios that could be happen at the end of attack phase.
   */
  def resultOfAttack(): MessageAttackPhase

  /**
   * @return the result of roll dice by attacker player.
   */
  def rollDiceAttacker(): Seq[Int]

  /**
   * @return the result of roll dice by defender player.
   */
  def rollDiceDefender(): Seq[Int]

  /**
   * method to set state from which starts the attack.
   *
   * @param state state from which starts the attack.
   */
  def setAttackerState(state: State): Unit

  /**
   * method to set the attacked state.
   *
   * @param state the attacked state.
   */
  def setDefenderState(state: State): Unit

  /**
   * @return the number of tanks to move in conquered state
   *
   */
  def numberOfTanksToMove(): Seq[Int]

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

  /**
   * method to set the default initial settings of attack phase.
   */
  def setDefaultAttackSettings: Unit

  /**
   * method to set the default initial settings of system, to restart a new game.
   */
  def setDefaultInitialSettings(): Unit

  /**
   * @return the type of map selected by the players
   */
  def typeOfMap(): VersionMap

  /**
   * @return the number of attacker player's dice
   *
   */
  def numberOfDiceAttacker(): Int

  /**
   * @return the number of defender player's dice
   *
   */
  def numberOfDiceDefender(): Int

  /**
   * @return the goal for win the game
   */
  def goal: Goal


}