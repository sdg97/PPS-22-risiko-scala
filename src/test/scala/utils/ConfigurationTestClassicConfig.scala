package utils

import model.config.SetupFromFiles
import model.entity.map.GameMap
import model.manager.VersionMap
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.junit.{Before, Test}

import java.io.File
import scala.io.Source

class ConfigurationTestClassicConfig:

  private val gameMap = new GameMap()

  @Before
  def before(): Unit =
    SetupFromFiles.setup(gameMap, VersionMap.Classic)

  @Test
  def testAllStatesAreLoadedClassicConfig(): Unit =
    val stateFile = new File("src/main/resources/config/states.txt")
    val numLines = Source.fromFile(stateFile).getLines().size
    assertEquals(gameMap.nodes.size, numLines)

  @Test
  def testAllBordersAreLoadedClassicConfig(): Unit =
    val bordersFile = new File("src/main/resources/config/borders.txt")
    val numLines = Source.fromFile(bordersFile).getLines().size
    assertEquals(gameMap.edges.size, numLines)

  @Test
  def testAllContinentsAreLoadedClassicConfig(): Unit =
    val continentFile = new File("src/main/resources/config/continents.txt")
    val numLines = Source.fromFile(continentFile).getLines().size
    assertEquals(gameMap.continents.size, numLines)
