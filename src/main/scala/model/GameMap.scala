package model

trait Graph:
  def nodes: Set[State]
  def edges: Set[(String,String)]
  def addEdge(state1: String, state2: String): Unit
  def addNode(state: State): Unit
  def getNeighborStates(state: String, player: Player): Set[String]
  def getNeighborStatesOfPlayer(state: String, player: Player): Set[String]
  def getStateByName(nameState: String): State
  def getPlayerStates(player: Player): Set[State]

class GameMap extends Graph:
  private var edgesSet = Set[(String,String)]()
  private var nodeSet = Set[State]()
  override def nodes: Set[State] = nodes
  override def edges: Set[(String,String)] = edgesSet
  override def addEdge(state1: String, state2: String): Unit = edgesSet += ((state1,state2))
  override def addNode(state: State): Unit = nodeSet += state

  override def getNeighborStates(state: String, player: Player): Set[String] = edgesSet collect {
    case (`state`, state2) if getStateByName(state2).player != player => state2
    case (state2, `state`) if getStateByName(state2).player != player => state2
  }
  override def getNeighborStatesOfPlayer(state: String, player: Player): Set[String] = edgesSet collect {
    case (`state`, state2) if getStateByName(state2).player == player => state2
    case (state2, `state`) if getStateByName(state2).player == player => state2
  }
  override def getStateByName(nameState: String): State = nodeSet.filter(s => s.name == nameState).head
  override def getPlayerStates(player: Player): Set[State] = nodeSet.filter(s => s.player == player)

object TryMap extends App:
  val map = new GameMap()
  val player1 = new PlayerImpl("pie", PlayerColor.Yellow)
  val player2 = new PlayerImpl("martin", PlayerColor.Blue)

  val italy = new StateImpl("italy", 3, player1)
  val france = new StateImpl("france", 3, player2)
  val swisse = new StateImpl("swisse", 5, player2)
  val austria = new StateImpl("austria", 5, player1)

  map.addNode(italy)
  map.addNode(france)
  map.addNode(swisse)
  map.addNode(austria)

  map.addEdge("italy","france")
  map.addEdge("italy","austria")
  map.addEdge("swisse","italy")

  val state = map.getStateByName("italy")
  println("Info stato: "+ state.name + " "+ state.player + " " + state.numberOfWagon)

  println("NeighborStates: ")
  map.getNeighborStates("italy", player1).foreach(s => println(s))

  println("Player NeighborStates: ")
  map.getNeighborStatesOfPlayer("italy", player1).foreach(s => println(s))

  println("PlayerStates: ")
  map.getPlayerStates(player1).foreach(s => println(s.name))