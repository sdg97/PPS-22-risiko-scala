package model

import java.io.File
import scala.io.Source

class ModelImpl() extends Model {
  val gameMap = new GameMap()
  val player1 = new PlayerImpl("pie", PlayerColor.Yellow)
  val player2 = new PlayerImpl("martin", PlayerColor.Blue)
  val player3 = new PlayerImpl("simo", PlayerColor.Red)

  val stateFile = new File("src/main/resources/config/states.txt")
  val stateFileLines: Seq[String] = Source.fromFile(stateFile).getLines().toList

  stateFileLines.foreach { line =>
    val parts = line.split(",")
    if (parts.length >= 3) {
      val name = parts(0).trim
      parts(1).trim
      parts(2).trim
      gameMap.addNode(new StateImpl(name))
    }
  }
  
  val borderFile = new File("src/main/resources/config/borders.txt")
  val borderFileLines: Seq[String] = Source.fromFile(borderFile).getLines().toList
  borderFileLines.foreach { line =>
    val parts = line.split(",")
    if (parts.length >= 2) {
      val state1 = parts(0).trim
      val state2 = parts(1).trim
      gameMap.addEdge(state1, state2)
    }
  }

  override def getNeighbor(stateName: String, player: Player): Set[String] = gameMap.getNeighborStates(stateName, player)

  override def getPlayerStates(player: Player): Set[State] = gameMap.getPlayerStates(player)

  override def getCurrentPlayer(): Player = player1

  private var setOfPlayer = Set[Player]()
  override def deployTroops(): Unit = ???

  override def setGameSettings(inputDataPlayer: Set[(String, Int)]): Unit =
    inputDataPlayer.foreach(element =>
      setOfPlayer = setOfPlayer + new PlayerImpl(element._1, PlayerColor.fromOrdinal(element._2))
    )

  override def getSetOfPlayers(): Set[Player] = setOfPlayer
}
