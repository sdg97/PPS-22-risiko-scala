package model.config

import model.config.Parsers.{BorderParser, ContinentParser, StateParser}
import model.entity.map.{Continent, GameMap, State}
import model.manager.VersionMap

import java.io.{File, FileInputStream}
import scala.io.Source

object SetupFromFiles:
  /**
   * method to configure from files of states, borders between states and continents
   * @param gameMap the game map to which you can add states and borders.
   * @param versionMap the version of the game map.
   */
  def setup(gameMap: GameMap, versionMap:VersionMap): Unit = {
    val resultVersionMap = setTypeOfMap(versionMap)
    readLinesFromFile(resultVersionMap._1).flatMap(StateParser.parse).foreach(gameMap.addNode)
    readLinesFromFile(resultVersionMap._2).flatMap(BorderParser.parse).foreach((state1, state2) => gameMap.addEdge(state1, state2))
    readLinesFromFile(resultVersionMap._3).flatMap(ContinentParser.parse).foreach(gameMap.addContinent)
  }

  /**
   * method to read all lines of a file
   *
   * @param filePath file in which read the lines
   * @return a Seq of all the lines of the file
   */
  private def readLinesFromFile(filePath: String): Seq[String] =
    Source.fromInputStream(getClass.getResourceAsStream(filePath)).getLines().toList

  /**
   * method to obtain the correct config files associated with the map version chosen by the user
   *
   * @param versionMap the version of the map chosen by the user
   * @return the config files associated with the map version
   */

  private def setTypeOfMap(versionMap:VersionMap) = versionMap match
    case VersionMap.Classic =>
      ("/config/states.txt", "/config/borders.txt", "/config/continents.txt")
    case VersionMap.Europe =>
      ("/config/states_europe.txt", "/config/borders_europe.txt","/config/continents_europe.txt")
    case _ => null