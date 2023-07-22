package utils

trait FSM:
  type Phase
  type Action

  def + (p1: Phase, a: Action, p2: Phase): Unit

  def trigger(a: Action): Phase

  def currentPhase: Phase

  def phases: Set[Phase]

  def next: Set[Phase]
  
  def permittedAction: Set[Action]

trait FSMImpl() extends FSM:
  private val g = new GraphWithEdgeImpl with TraversableGraph:
    override type Node = Phase
    override type Edge = Action
  override def +(p1: Phase, a: Action, p2: Phase): Unit =
    g.addEdge(p1,p2,a)
  override def trigger(a: Action): Phase =
    println(s"TRIGGER ${a} ${g.getCurrentNode()}")
    g.crossEdge(a)
    g.getCurrentNode().get
  override def currentPhase: Phase =
    g.getCurrentNode().get
  override def next: Set[Phase] =
    g.getNeighbours(g.getCurrentNode().get)
  override def phases: Set[Phase] = g.nodes
  override def permittedAction: Set[Action] = g.outEdges(g.getCurrentNode().get)



