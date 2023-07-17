package utils

import model.Player

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
  private var currentNode : Option[Node] = None
  private var first = true
  abstract override def addEdge(n1: Node, n2: Node, e: Edge) =
    currentNode = if first then Some(n1) else currentNode
    first = false
    super.addEdge(n1,n2,e)
  def setCurrentNode(n: Node) =
    //lancia comunque un eccezione
    currentNode = if g.nodes contains n then Some(n) else currentNode
  def getCurrentNode() = currentNode

  //deve tirare un'eccezione quando non ci sono
  def crossEdge(toCross: Edge) =
    val t = Some(getNeighbours(currentNode.get, toCross).head)
    currentNode = if !t.isEmpty then t else currentNode

object TryGraph extends App:
  val g = new GraphWithEdgeImpl with TraversableGraph:
    override type Node = String
    override type Edge = Int

  g.addEdge("a", "b", 2)
  g.addEdge("b", "c", 3)
  println(g.getCurrentNode())
  g.crossEdge(2)
  println(g.getCurrentNode())
  g.crossEdge(3)
