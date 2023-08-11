package utils

import model.entity.Player

trait Graph:
  type Node
  def nodes: Set[Node]
  def edges: Set[(String,String)]
  def addEdge(node1: String, node2: String): Unit
  def addNode(node: Node): Unit

trait GraphWithEdge:
  type Node   // Node and Edge are abstract types, member of Graph
  type Edge
  def addEdge(n1: Node, n2: Node, e: Edge): Unit
  def nodes: Set[Node]
  def outEdges(n: Node): Set[Edge]
  def inEdges(n: Node): Set[Edge]
  def getNeighbours(n: Node, e: Edge): Set[Node]
  def getNeighbours(n: Node): Set[Node]

trait GraphWithEdgeImpl() extends GraphWithEdge:
  private var data = Set[(Node,Edge,Node)]()
  override def addEdge(n1: Node, n2: Node, e: Edge) = data += ((n1,e,n2))
  override def nodes = (data map (_._1)) ++ (data map (_._3))
  override def outEdges(n: Node) = data collect { case (`n`,e,_) => e }
  override def inEdges(n: Node) = data collect { case (_,e,`n`) => e }
  override def getNeighbours(n: Node, e: Edge): Set[Node] = data collect {case(`n`,`e`, n) => n}
  override def getNeighbours(n: Node): Set[Node] = data collect {case(`n`,_, n) => n}


trait TraversableGraph extends GraphWithEdge:
  g: GraphWithEdge =>
  private var _currentNode : Option[Node] = None
  private var first = true
  abstract override def addEdge(n1: Node, n2: Node, e: Edge) =
    val n1Neighbours = getNeighbours(n1,e)
    if !n1Neighbours.isEmpty
      then throw CantConnectTwoDifferentNodeWithTheSameEdge(n1,n1Neighbours.head, n2, e)
    _currentNode = if first then Some(n1) else _currentNode
    first = false
    super.addEdge(n1,n2,e)
  def currentNode(n: Node) =
    if !g.nodes.contains(n) then
      throw NodeNotFound(n)
    _currentNode = Some(n)
  def currentNode = _currentNode

  def crossEdge(toCross: Edge) =
    val n = getNeighbours(_currentNode.get, toCross)
    if !n.isEmpty then _currentNode = Some(n.head) else throw NoEdgeToCross("toCross")

case class NoEdgeToCross[X](e:X)
  extends Exception (s"No edge to cross from this node ${e}")
case class CantConnectTwoDifferentNodeWithTheSameEdge[X,Y](source: X, n1: X, n2: X, edge: Y)
  extends Exception(s"Can't connect ${n1} and ${n2} with ${source} using the same edge")
case class NodeNotFound[X](n:X)
  extends Exception(s"Node ${n} not found in the graph")

