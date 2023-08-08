package utils

import model.{Continent, GameMap, State, VersionMap}

import java.io.File
import scala.io.Source

object SetupFromFiles:

  /**
   * method to configure from files of states, borders between states and continents
   * @param gameMap the game map to which you can add states and borders.
   * @param versionMap the version of the game map.
   */
  def setup(gameMap: GameMap, versionMap:VersionMap): Unit = {
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

    def setTypeOfMap(): (String, String, String) = versionMap match
      case typeMap if typeMap==(VersionMap.Classic) =>
        ("src/main/resources/config/states.txt", "src/main/resources/config/borders.txt", "src/main/resources/config/continents.txt")
      case typeMap if typeMap==(VersionMap.Europe) =>
        ("src/main/resources/config/states_europe.txt", "src/main/resources/config/borders_europe.txt", "src/main/resources/config/continents_europe.txt")
      case _ => null

    val resultVersionMap=setTypeOfMap()

    val stateFile = new File(resultVersionMap._1)
    val borderFile = new File(resultVersionMap._2)
    val continentFile = new File(resultVersionMap._3)
    

    readLinesFromFile(stateFile).flatMap(parseState).foreach(gameMap.addNode)
    readLinesFromFile(borderFile).flatMap(parseBorder).foreach { (state1, state2) => gameMap.addEdge(state1, state2) }
    readLinesFromFile(continentFile).flatMap(parseContinent).foreach(gameMap.addContinent)
  }