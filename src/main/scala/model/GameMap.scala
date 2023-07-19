package model

import utils.Graph

class GameMap extends Graph:
  override type Node = State
  private var edgesSet = Set[(String,String)]()
  private var nodeSet = Set[State]()
  override def nodes: Set[State] = nodeSet
  override def edges: Set[(String,String)] = edgesSet
  override def addEdge(state1: String, state2: String): Unit = edgesSet += ((state1,state2))
  override def addNode(state: State): Unit = nodeSet += state

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
    val states: Seq[State] = nodeSet.toSeq
    assign(players.toSeq, states).foreach { t => t._2.foreach { s => s.setPlayer(t._1) } }
    
    nodeSet.toSeq.map(s => s"${s.name} ${s.player.username}").foreach(println(_))

  def wagonToPlace(player: Player): Int = getPlayerStates(player).size/3
/*
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
*/