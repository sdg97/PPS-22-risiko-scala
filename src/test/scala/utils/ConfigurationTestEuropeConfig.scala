package utils

import model.config.SetupFromFiles
import model.entity.map.GameMap
import model.manager.VersionMap
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.junit.{Before, Test}

import java.io.File
import scala.io.Source

class ConfigurationTestEuropeConfig:

  private val gameMap = new GameMap()

  @Before
  def before(): Unit =
    SetupFromFiles.setup(gameMap, VersionMap.Europe)

  @Test
  def testAllStatesAreLoadedEuropeConfig(): Unit =
    val stateFile = new File("src/main/resources/config/states_europe.txt")
    val numLines = Source.fromFile(stateFile).getLines().size
    assertEquals(gameMap.nodes.size, numLines)

  @Test
  def testAllBordersAreLoadedEuropeConfig(): Unit =
    val bordersFile = new File("src/main/resources/config/borders_europe.txt")
    val numLines = Source.fromFile(bordersFile).getLines().size
    assertEquals(gameMap.edges.size, numLines)

  @Test
  def testAllContinentsAreLoadedEuropeConfig(): Unit =
    val continentFile = new File("src/main/resources/config/continents_europe.txt")
    val numLines = Source.fromFile(continentFile).getLines().size
    assertEquals(gameMap.continents.size, numLines)