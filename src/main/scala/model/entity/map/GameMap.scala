package model.entity.map

import model.entity.map.Continent
import model.manager.VersionMap
import model.entity.Player
import utils.Graph

class GameMap extends Graph:
  override type Node = State
  private var _edges = Set[(StateName, StateName)]()
  private var _nodes = Set[State]()
  private var _continents = Set[Continent]()

  /**
   *
   * @return the Set of all states in the game map.
   */
  override def nodes: Set[State] = _nodes

  /**
   *
   * @return the Set of all pairs of state names, representing a state borders
   */
  override def edges: Set[(StateName, StateName)] = _edges

  /**
   * method to create a edge between two states
   * @param stateName1 the first state to link
   * @param stateName2 the second state to link
   */
  override def addEdge(stateName1: StateName, stateName2: StateName): Unit = _edges += (stateName1, stateName2)

  /**
   * method to add a new state to the map
   * @param state the state to add to the map
   */
  override def addNode(state: State): Unit = _nodes += state

  /**
   *
   * @return the Set of all continents
   */
  def continents: Set[Continent] = _continents

  /**
   * method to add a new continent
   *
   * @param continent the continent to add
   */
  def addContinent(continent: Continent): Unit = _continents += continent

  /**
   * @param stateName the state's name from which to search for neighbors
   * @param player the current player
   * @return a Set of the names of neighboring states with the state passed as a parameter and owned by enemy players
   */
  def neighborStatesOfEnemies(stateName: StateName, player: Player): Set[StateName] = neighborStates(stateName) filterNot(s => isPlayerState(s, player))

  /**
   * @param stateName the state's name from which to search for neighbors
   * @param player    the current player
   * @return a Set of the names of neighboring states with the state passed as a parameter and owned by the player passed as a parameter
   */
  def neighborStatesOfPlayer(stateName: StateName, player: Player): Set[StateName] = neighborStates(stateName) filter(s => isPlayerState(s, player))

  /**
   * @param stateName the state's name from which to search for neighbors
   * @return the state instance with the name passed as a parameter
   */
  def stateByName(stateName: StateName): State = _nodes.filter(_.name == stateName).head

  /**
   * @param player    the current player
   * @return a Set of all states owned by the player passed as a parameter
   */
  def playerStates(player: Player): Set[State] = _nodes.filter(_.player == player)
  
  def assignStatesToPlayers(players: List[Player], versionMap: VersionMap): Unit =
    import utils.AssignGivenInstances.given
    players assign nodes
    players.foreach(p =>
      playerStates(p) assign players.getStartTankNumber(versionMap)
    )

  /**
   *
   * calculates and assigns to a player the number of tanks he can place
   * @param player the current player
   */
  def calcTanksToPlace(player: Player): Unit =
    val playerStatesSet = playerStates(player)
    val continentBonus = _continents.filter(continent => continent.states.subsetOf(playerStatesSet.map(_.name))).map(_.bonus).sum
    player.setTanksToPlace(playerStatesSet.size / 3 + continentBonus)

  /**
   *
   * moves tanks from a state to another state
   * @param fromStateName starting state
   * @param toStateName arrival status
   * @param numberOfTanks the number of tanks to move
   */
  def moveTanks(fromStateName: StateName, toStateName: StateName, numberOfTanks: Int): Unit =
    stateByName(fromStateName).removeTanks(numberOfTanks)
    stateByName(toStateName).addTanks(numberOfTanks)

  /**
   * @param stateName the state name to check
   * @param player the current player
   * @return a boolean representing whether the state passed as a parameter is of the player passed as a parameter
   */
  private def isPlayerState(stateName: StateName, player: Player): Boolean = stateByName(stateName).player == player

  /**
   * @param stateName the state name to check
   * @return a Set of state names representing the neighboring states of the state passed as a parameter
   */
  private def neighborStates(stateName: StateName) = _edges collect {
    case (`stateName`, state2) => state2
    case (state2, `stateName`) => state2
  }