package model.entity.map

import model.entity.map.{Continent, GameMap, State}
import model.entity.{Player, PlayerColor}
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.junit.{Before, Test}

class GameMapTest:

  private val gameMap = new GameMap()
  private val player1 = Player("pie", PlayerColor.YELLOW)
  private val player2 = Player("martin", PlayerColor.BLUE)
  private val player3 = Player("simo", PlayerColor.BLUE)

  private val italy = State("italy", 3, player1)
  private val france = State("france", 3, player2)
  private val germany = State("germany", 3, player1)
  private val brazil = State("brazil", 5, player1)

  private val europa = Continent("europa", Set("italy", "germany", "france"), 4)

  @Before
  def before(): Unit =
    gameMap.addNode(italy)
    gameMap.addNode(brazil)
    gameMap.addNode(france)
    gameMap.addNode(germany)
    gameMap.addEdge("italy", "france")
    gameMap.addEdge("italy", "germany")
    gameMap.addContinent(europa)

  @Test
  def testPlayerState(): Unit =
    assert(gameMap.playerStates(player1).contains(italy))
    assert(gameMap.playerStates(player1).contains(brazil))

  @Test
  def testGetStateByName(): Unit =
    assertEquals(gameMap.stateByName("italy"), italy)

  @Test
  def testAddContinent(): Unit =
    assert(gameMap.continents.contains(europa))

  @Test
  def testCalcTanksToPlaceWithoutContinent(): Unit =
    gameMap.calcTanksToPlace(player1)
    assertEquals(player1.tanksToPlace,1)

  @Test
  def testCalcTanksToPlaceWithContinent(): Unit =
    gameMap.stateByName("france").setPlayer(player1)
    gameMap.calcTanksToPlace(player1)
    assertEquals(player1.tanksToPlace, 5)

  @Test
  def testAddTanks(): Unit =
    assertEquals(gameMap.stateByName("italy").numberOfTanks, 3)
    gameMap.stateByName("italy").addTanks(1)
    assertEquals(gameMap.stateByName("italy").numberOfTanks, 4)

  @Test
  def testMoveTanks(): Unit =
    assertEquals(gameMap.stateByName("italy").numberOfTanks, 3)
    gameMap.moveTanks("italy", "france", 2)
    assertEquals(gameMap.stateByName("italy").numberOfTanks, 1)
    assertEquals(gameMap.stateByName("france").numberOfTanks, 5)


  @Test
  def testNeighborsOfEnemies(): Unit =
    assert(gameMap.neighborStatesOfEnemies("italy", player1).contains("france"))
    assertFalse(gameMap.neighborStatesOfEnemies("italy", player1).contains("brazil"))
    assertFalse(gameMap.neighborStatesOfEnemies("italy", player1).contains("germany"))

  @Test
  def testNeighborsOfPlayer(): Unit =
    assert(gameMap.neighborStatesOfPlayer("italy", player1).contains("germany"))
    assertFalse(gameMap.neighborStatesOfPlayer("italy", player1).contains("france"))
    assertFalse(gameMap.neighborStatesOfPlayer("italy", player1).contains("brazil"))
