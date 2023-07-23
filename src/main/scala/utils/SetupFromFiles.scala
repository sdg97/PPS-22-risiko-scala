package utils

import model.{GameMap, StateImpl}

import java.io.File
import scala.io.Source

object SetupFromFiles {
  def setup(gameMap: GameMap): Unit =
    val stateFile = new File("src/main/resources/config/states.txt")
    val stateFileLines: Seq[String] = Source.fromFile(stateFile).getLines().toList
    stateFileLines.foreach { line =>
      val parts = line.split(",")
      if (parts.length >= 3) {
        val name = parts(0).trim
        val posX = parts(1).trim
        val posY = parts(2).trim
        gameMap.addNode(new StateImpl(name, 0, null, posX.toInt, posY.toInt))
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
/*
    val continentFile = new File("src/main/resources/config/continent.txt")
    val continentFileLines: Seq[String] = Source.fromFile(continentFile).getLines().toList
    borderFileLines.foreach { line =>
      val parts = line.split(",")
      val continentName = parts(0).trim
      var continentStates: Set[String]
      val x = 1
      for x <- parts.length {
        continentStates+(parts(x).trim)
      }
    }
*/
}
