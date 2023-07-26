package model
import utils.Graph

class GameMap extends Graph:
  override type Node = State
  private var _edges = Set[(String,String)]()
  private var _nodes = Set[State]()
  private var _continents = Set[Continent]()

  override def nodes: Set[State] = _nodes
  override def edges: Set[(String,String)] = _edges
  override def addEdge(state1: String, state2: String): Unit = _edges += (state1,state2)
  override def addNode(state: State): Unit = _nodes += state

  def continents: Set[Continent] = _continents
  def addContinent(continent: Continent): Unit = _continents += continent

  def neighborStatesOfEnemies(stateName: String, player: Player): Set[String] = neighborStates(stateName) filterNot(s => isPlayerState(s, player))

  def neighborStatesOfPlayer(stateName: String, player: Player): Set[String] = neighborStates(stateName) filter(s => isPlayerState(s, player))

  def stateByName(nameState: String): State = _nodes.filter(_.name == nameState).head

  def playerStates(player: Player): Set[State] = _nodes.filter(_.player == player)

  def assignStatesToPlayers(players: Set[Player]) =
    import utils.AssignGivenInstances.given
    players assign nodes
    players.foreach(p =>
      playerStates(p) assign players.START_TANK_NUMBER
    )

  def calcTanksToPlace(player: Player): Unit =
    val playerStatesSet = playerStates(player)
    val continentBonus = _continents.filter(continent => continent.states.subsetOf(playerStatesSet.map(_.name))).map(_.bonus).sum
    player.setTanksToPlace(playerStatesSet.size / 3 + continentBonus)

  def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit =
    stateByName(fromStateName).removeTanks(numberOfTanks)
    stateByName(toStateName).addTanks(numberOfTanks)

  private def isPlayerState(stateName: String, player: Player): Boolean = stateByName(stateName).player == player

  private def neighborStates(state: String) = _edges collect {
    case (`state`, state2) => state2
    case (state2, `state`) => state2
  }