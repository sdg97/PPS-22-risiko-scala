package model
import utils.Graph


class GameMap extends Graph:
  override type Node = State
  private var edgesSet = Set[(String,String)]()
  private var nodeSet = Set[State]()
  private var continentSet = Set[Continent]()

  override def nodes: Set[State] = nodeSet
  override def edges: Set[(String,String)] = edgesSet
  override def addEdge(state1: String, state2: String): Unit = edgesSet += ((state1,state2))
  override def addNode(state: State): Unit = nodeSet += state
  def continents: Set[Continent] = continentSet
  def addContinent(continent: Continent): Unit = continentSet += continent

  def getNeighborStates(state: String, player: Player): Set[String] = edgesSet collect {
      case (`state`, state2) if getStateByName(state2).player != player => state2
      case (state2, `state`) if getStateByName(state2).player != player => state2
  }
  def getNeighborStatesOfPlayer(state: String, player: Player): Set[String] = edgesSet collect {
    case (`state`, state2) if getStateByName(state2).player == player => state2
    case (state2, `state`) if getStateByName(state2).player == player => state2
  }
  def getStateByName(nameState: String): State = nodeSet.filter(s => s.name == nameState).head
  def getPlayerStates(player: Player): Set[State] = nodeSet.filter(s => s.player.username.equals(player.username))

  def assignStatesToPlayers(players: Set[Player]) =
    import utils.Assign.assign
    import utils.AssignableGivenInstances.given
    import utils.AssignExtensionGivenInstances.given
    val states: Seq[State] = nodeSet.toSeq
    assign(players.toSeq, states).foreach { t => t._2.foreach { s => s.setPlayer(t._1) } }
    players.foreach(p =>
      val playerStates = getPlayerStates(p)
      playerStates.toSeq.assign(players.START_TANK_NUMBER)
    )
    nodeSet.toSeq.map(s => s"${s.name} ${s.player.username} ${s.numberOfWagon}").foreach(println(_))

  def calcWagonToPlace(player: Player): Unit =
    var wagonToPlace = getPlayerStates(player).size / 3
    val allContinent = false
    val playerStatesName = getPlayerStates(player).map(_.name)
    continentSet.foreach(c => {
      if(c.states.subsetOf(playerStatesName))
        c.name match {
          case "oceania" | "sud america" => wagonToPlace = wagonToPlace + 2
          case "africa" => wagonToPlace = wagonToPlace + 3
          case "europa" | "nord america" => wagonToPlace = wagonToPlace + 5
          case "asia" => wagonToPlace = wagonToPlace + 7
        }
    })
    player.setWagonToPlace(wagonToPlace)

  def shiftWagon(fromStateName: String, toStateName: String, numberOfWagon: Int): Unit =
    getStateByName(fromStateName).removeWagon(numberOfWagon)
    getStateByName(toStateName).addWagon(numberOfWagon)


