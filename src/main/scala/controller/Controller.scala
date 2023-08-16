package controller

import model.entity.{Goal, Player}
import model.entity.map.State
import model.manager.{MessageAttackPhase, RisikoAction, RisikoPhase, RisikoSwitchPhaseAction, SettingResult, VersionMap}

trait Controller:
  def startNewGame(): Unit

  def setGameSettings(inputDataPlayer: List[(String, String)], typeOfMap: String): SettingResult

  /**
   * @param stateName the state's name from which to search for neighbors
   * @return a Set of the names of neighboring states with the state passed as a parameter and owned by enemy players
   */
  def neighborStatesOfEnemies(stateName: String): Set[String]

  /**
   * @param stateName the state's name from which to search for neighbors
   * @return a Set of the names of neighboring states with the state passed as a parameter and owned by the player passed as a parameter
   */
  def neighborStatesOfPlayer(state: String): Set[String]

  /**
   * @param stateName the name of the state
   * @return the state object
   */
  def stateByName(stateName: String): State

  /**
   *
   * @return a Set of all states owned by the current player
   */
  def currentPlayerStates: Set[State]

  /**
   *
   * @return the current player
   */
  def currentPlayer: Player

  /**
   *
   * @return a Set of all the states
   */
  def allStates: Set[State]

  /**
   * method to update the view with updated state's data
   */
  def updateView(): Unit

  /**
   * @param stateName the name of the state to assign the tanks to
   * @return a Set of all states owned by the current player
   */
  def addTank(stateName: String): Unit

  /**
   * @return the number of tanks that the current player can place.
   */
  def tanksToPlace: Int

  def switchTurnPhaseActionAvailable: Set[RisikoAction]

  def switchPhase(a: RisikoSwitchPhaseAction): Unit

  def resultAttack(): MessageAttackPhase

  def attackPhase(): Unit

  def numberOfDiceForPlayers(attacker: State, defender: State): (Int, Int)

  /**
   *
   * moves tanks from a state to another state
   *
   * @param fromStateName starting state
   * @param toStateName   arrival status
   * @param numberOfTanks the number of tanks to move
   */
  def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit

  def numberOfTanksToMove(attacker: State): Int

  def currentTurnPhase: RisikoPhase

  def setAttacker(state: State): Unit

  def setDefender(state: State): Unit

  def rollDiceAttacker(): Seq[Int]

  def rollDiceDefender(): Seq[Int]

  def setDefaultAttackSettings: Unit

  def getAttacker(): State

  def getDefender(): State

  def showGameView: Unit

  def setDefaultInitialSettings(): Unit

  def setTypeOfMap(): VersionMap

  def goal: Goal
