package utils

import model.entity.Player

/**
 * A lean representation of a graph, where the concept of
 * edge between two nodes is of an unrelevant type
 */
trait Graph:
  /**
   * Graph node type
   */
  type Node

  /**
   *
   * @return all the Graph nodes
   */
  def nodes: Set[Node]

  /**
   *
   * @return all the graph edges
   */
  def edges: Set[(String,String)]

  /**
   * Add a new connection between two nodes
   * @param node1: the first node to connect
   * @param node2: the second node to connect
   */
  def addEdge(node1: String, node2: String): Unit

  /**
   * Add a new node
   * @param node: the node to add
   */
  def addNode(node: Node): Unit

/**
 * Graph representation where node and edge between node has specific types to define
 */
trait GraphWithEdge:
  /**
   * Graph node type
   */
  type Node
  /**
   * Edge node type
   */
  type Edge

  /**
   * Add a new connection between two nodes
   *
   * @param node1 : the first node to connect
   * @param node2 : the second node to connect
   * @param edge: the edge between the nodes
   */
  def addEdge(node1: Node, node2: Node, edge: Edge): Unit

  /**
   *
   * @return all the Graph nodes
   */
  def nodes: Set[Node]

  /**
   * @param node
   * @return the edges from a specific node
   */
  def outEdges(node: Node): Set[Edge]

  /**
   * @param node
   * @return the edges that leads to a specific node
   */
  def inEdges(node: Node): Set[Edge]

  /**
   * @param node
   * @param edge
   * @return the reachable nodes given the node and the edge
   */
  def getNeighbours(node: Node, edge: Edge): Set[Node]

  /**
   *
   * @param node
   * @return the reachable node from the node
   */
  def getNeighbours(node: Node): Set[Node]

/**
 * Implementation of a graph with edge behavior
 */
trait GraphWithEdgeImpl() extends GraphWithEdge:
  private var data = Set[(Node,Edge,Node)]()
  override def addEdge(n1: Node, n2: Node, e: Edge) = data += ((n1,e,n2))
  override def nodes = (data map (_._1)) ++ (data map (_._3))
  override def outEdges(n: Node) = data collect { case (`n`,e,_) => e }
  override def inEdges(n: Node) = data collect { case (_,e,`n`) => e }
  override def getNeighbours(n: Node, e: Edge): Set[Node] = data collect {case(`n`,`e`, n) => n}
  override def getNeighbours(n: Node): Set[Node] = data collect {case(`n`,_, n) => n}


/**
 * A graph with edge that can be traversed
 */
trait TraversableGraph extends GraphWithEdge:
  g: GraphWithEdge =>
  private var _currentNode : Option[Node] = None
  private var first = true

  /**
   * Add a new graph edge. If it is the first add operation node1 became the current
   * node. Check than two different nodes can't be connected by the same edge.
   * If the check not pass an exception will throw.
   * @param node1: first node to add
   * @param node2: second node to add
   * @param edge: edge between the nodes
   */
  abstract override def addEdge(node1: Node, node2: Node, edge: Edge) =
    val n1Neighbours = getNeighbours(node1,edge)
    if !n1Neighbours.isEmpty
      then throw CantConnectTwoDifferentNodeWithTheSameEdge(node1,n1Neighbours.head, node2, edge)
    _currentNode = if first then Some(node1) else _currentNode
    first = false
    super.addEdge(node1,node2,edge)

  /**
   * Set the current node.
   * The node must be one of the graph node. If it's not an
   * exception will throw.
   * @param node the node to set
   */
  def currentNode(node: Node) =
    if !g.nodes.contains(node) then
      throw NodeNotFound(node)
    _currentNode = Some(node)

  /**
   * @return the current node
   */
  def currentNode = _currentNode

  /**
   * Cross an edge from the current node and go to another.
   * @param toCross: the edge to cross
   */
  def crossEdge(toCross: Edge) =
    val n = getNeighbours(_currentNode.get, toCross)
    if !n.isEmpty then _currentNode = Some(n.head) else throw NoEdgeToCross("toCross")

case class NoEdgeToCross[X](e:X)
  extends Exception (s"No edge to cross from this node ${e}")
case class CantConnectTwoDifferentNodeWithTheSameEdge[X,Y](source: X, n1: X, n2: X, edge: Y)
  extends Exception(s"Can't connect ${n1} and ${n2} with ${source} using the same edge")
case class NodeNotFound[X](n:X)
  extends Exception(s"Node ${n} not found in the graph")

