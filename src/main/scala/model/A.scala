package model

trait A:
  type Node   // Node and Edge are abstract types, member of Graph
  type Edge
  def addEdge(n1: Node, n2: Node, e: Edge): Unit
  def nodes: Set[Node]
  def outEdges(n: Node): Set[Edge]
  def inEdges(n: Node): Set[Edge]
  def getNeighbours(n: Node, e: Edge): Set[Node]

trait AImpl() extends A:
  private var data = Set[(Node,Edge,Node)]()
  override def addEdge(n1: Node, n2: Node, e: Edge) = data += ((n1,e,n2))
  override def nodes = (data map (_._1)) ++ (data map (_._3))
  override def outEdges(n: Node) = data collect { case (`n`,e,_) => e }
  override def inEdges(n: Node) = data collect { case (_,e,`n`) => e }
  override def getNeighbours(n: Node, e: Edge): Set[Node] = data collect {case(`n`,`e`, n) => n}


trait TraversableGraph extends A:
  g: A =>
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
  val g = new AImpl with TraversableGraph:
    override type Node = String
    override type Edge = Int

  g.addEdge("a", "b", 2)
  g.addEdge("b", "c", 3)
  println(g.getCurrentNode())
  g.crossEdge(2)
  println(g.getCurrentNode())
  g.crossEdge(3)
