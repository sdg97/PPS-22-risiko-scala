package model
import utils.Graph

class GameMap extends Graph:
  override type Node = State
  private var _edges = Set[(String,String)]()
  private var _nodes = Set[State]()
  private var _continents = Set[Continent]()

  override def nodes: Set[State] = _nodes
  override def edges: Set[(String,String)] = _edges
  override def addEdge(state1: String, state2: String): Unit = _edges += ((state1,state2))
  override def addNode(state: State): Unit = _nodes += state
  def continents: Set[Continent] = _continents
  def addContinent(continent: Continent): Unit = _continents += continent

  def neighborStates(state: String, player: Player): Set[String] = _edges collect {
      case (`state`, state2) if stateByName(state2).player != player => state2
      case (state2, `state`) if stateByName(state2).player != player => state2
  }
  def neighborStatesOfPlayer(state: String, player: Player): Set[String] = _edges collect {
    case (`state`, state2) if stateByName(state2).player == player => state2
    case (state2, `state`) if stateByName(state2).player == player => state2
  }
  def stateByName(nameState: String): State = _nodes.filter(s => s.name == nameState).head
  def playerStates(player: Player): Set[State] = _nodes.filter(s => s.player.username.equals(player.username))

  def assignStatesToPlayers(players: Set[Player]) =
    import utils.AssignGivenInstances.given
    players assign nodes
    players.foreach(p =>
      playerStates(p) assign players.START_TANK_NUMBER
    )

  def calcWagonToPlace(player: Player): Unit =
    var wagonToPlace = playerStates(player).size / 3
    val allContinent = false
    val playerStatesName = playerStates(player).map(_.name)
    _continents.foreach(c => {
      if(c.states.subsetOf(playerStatesName))
        c.name match {
          case "oceania" | "sud america" => wagonToPlace = wagonToPlace + 2
          case "africa" => wagonToPlace = wagonToPlace + 3
          case "europa" | "nord america" => wagonToPlace = wagonToPlace + 5
          case "asia" => wagonToPlace = wagonToPlace + 7
        }
    })
    player.setTanksToPlace(wagonToPlace)

  def shiftWagon(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit =
    stateByName(fromStateName).removeWagon(numberOfWagon)
    stateByName(toStateName).addWagon(numberOfWagon)


