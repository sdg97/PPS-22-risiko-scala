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

  def stateByName(nameState: String): State = _nodes.filter(s => s.name.equals(nameState)).head

  def playerStates(player: Player): Set[State] = _nodes.filter(s => isPlayerState(s.name, player))

  def assignStatesToPlayers(players: Set[Player]) =
    import utils.AssignGivenInstances.given
    players assign nodes
    players.foreach(p =>
      playerStates(p) assign players.START_TANK_NUMBER
    )

  def calcWagonToPlace(player: Player): Unit =
    var wagonToPlace = playerStates(player).size / 3
    val playerStatesName = playerStates(player).map(_.name)
    _continents.foreach(continent => {
      if(continent.states.subsetOf(playerStatesName))
        wagonToPlace = wagonToPlace + continent.bonus
    })
    player.setTanksToPlace(wagonToPlace)

  def moveWagon(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit =
    stateByName(fromStateName).removeWagon(numberOfWagon)
    stateByName(toStateName).addWagon(numberOfWagon)

  private def isPlayerState(stateName: String, player: Player): Boolean = stateByName(stateName).player.equals(player)

  private def neighborStates(state: String) = _edges collect {
    case (`state`, state2) => state2
    case (state2, `state`) => state2
  }