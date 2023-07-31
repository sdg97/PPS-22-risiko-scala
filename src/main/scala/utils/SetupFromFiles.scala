package utils

import model.{Continent, GameMap, State}

import java.io.File
import scala.io.Source

object SetupFromFiles:
  def setup(gameMap: GameMap): Unit = {
    def readLinesFromFile(file: File): Seq[String] = Source.fromFile(file).getLines().toList

    def parseState(stateLine: String): Option[State] =
      stateLine.split(",").toList match {
        case name :: posX :: posY :: Nil => Some(State(name.trim, 0, null, posX.trim.toInt, posY.trim.toInt))
        case _ => None
      }

    def parseBorder(borderLine: String): Option[(String, String)] =
      borderLine.split(",").toList match {
        case state1 :: state2 :: Nil => Some((state1.trim, state2.trim))
        case _ => None
      }

    def parseContinent(continentLine: String): Option[Continent] =
      continentLine.split(",").toList match {
        case continentName :: other if other.nonEmpty =>
          val continentStates = other.dropRight(1).map(_.trim).toSet
          val bonus = other.last.trim.toInt
          Some(Continent(continentName.trim, continentStates, bonus))
        case _ => None
      }

//    val stateFile = new File("src/main/resources/config/states_europe.txt")
//    val borderFile = new File("src/main/resources/config/borders_europe.txt")
//    val continentFile = new File("src/main/resources/config/continents_europe.txt")

    val stateFile = new File("src/main/resources/config/states_europe.txt")
    val borderFile = new File("src/main/resources/config/borders_europe.txt")
    val continentFile = new File("src/main/resources/config/continents_europe.txt")

    readLinesFromFile(stateFile).flatMap(parseState).foreach(gameMap.addNode)
    readLinesFromFile(borderFile).flatMap(parseBorder).foreach { (state1, state2) => gameMap.addEdge(state1, state2) }
    readLinesFromFile(continentFile).flatMap(parseContinent).foreach(gameMap.addContinent)
  }