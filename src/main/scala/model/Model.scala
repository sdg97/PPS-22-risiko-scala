package model

class Model() {

  val gameMap = new GameMap()
  val player1 = new PlayerImpl("pie", PlayerColor.Yellow)
  val player2 = new PlayerImpl("martin", PlayerColor.Blue)
  val player3 = new PlayerImpl("simo", PlayerColor.Red)

  val italy = new StateImpl("italy", 3, player1)
  val france = new StateImpl("france", 3, player2)
  val swisse = new StateImpl("swisse", 5, player2)
  val brazil = new StateImpl("brazil", 5, player3)
  val argentina = new StateImpl("argentina", 5, player3)
  val chile = new StateImpl("chile", 5, player1)

  gameMap.addNode(italy)
  gameMap.addNode(france)
  gameMap.addNode(swisse)
  gameMap.addNode(brazil)
  gameMap.addNode(argentina)
  gameMap.addNode(chile)

  gameMap.addEdge("italy", "france")
  gameMap.addEdge("swisse", "italy")
  gameMap.addEdge("swisse", "france")
  gameMap.addEdge("brazil", "argentina")
  gameMap.addEdge("brazil", "chile")
  gameMap.addEdge("argentina", "chile")

  def setGameSettings() = println("game settings")
  def deployTroops() =  println("Troop deployed")

  def getNeighbor(stateName: String, player: Player): Set[String] = gameMap.getNeighborStates(stateName, player)
  def getPlayerStates(player: Player): Set[State] = gameMap.getPlayerStates(player)
  def getCurrentPlayer(): Player = player1
}
