package utils

/**
 * Final State Machine
 */
trait FSM:
  /**
   * FSM states type
   */
  type Phase
  /**
   * FSM actions type that trigger the movement from a phase to another
   */
  type Action

  /**
   * Add a new phases link. The function add the new phases to the FSM.
   * @param p1 first phase
   * @param a action that trigger the movement from the first phase to the second
   * @param p2 second phase
   */
  def + (p1: Phase, a: Action, p2: Phase): Unit

  /**
   * Trigger the movement from a phase to another
   * @param action
   * @return the new current phase
   */
  def trigger(action: Action): Phase

  /**
   *
   * @return the current phase
   */
  def currentPhase: Phase

  /**
   *
   * @return all the FSM phases
   */
  def phases: Set[Phase]

  /**
   *
   * @return the next phases reachable from the current
   */
  def next: Set[Phase]

  /**
   *
   * @return the actions allowed in this phase
   */
  def permittedAction: Set[Action]

/**
 * Implementation of FSM. Need Phase and Action specification during an instance creation
 */
trait FSMImpl() extends FSM:
  private val g = new GraphWithEdgeImpl with TraversableGraph:
    override type Node = Phase
    override type Edge = Action
  override def +(p1: Phase, a: Action, p2: Phase): Unit =
    g.addEdge(p1,p2,a)
  override def trigger(a: Action): Phase =
    g.crossEdge(a)
    g.currentNode.get
  override def currentPhase: Phase =
    g.currentNode.get
  override def next: Set[Phase] =
    g.getNeighbours(g.currentNode.get)
  override def phases: Set[Phase] = g.nodes
  override def permittedAction: Set[Action] = g.outEdges(g.currentNode.get)



