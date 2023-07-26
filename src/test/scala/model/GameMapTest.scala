package model

import org.junit.{Before, Test}
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}

class GameMapTest:

  private val map = new GameMap()
  private val player1 = Player("pie", PlayerColor.YELLOW)
  private val player2 = Player("martin", PlayerColor.BLUE)
  private val player3 = Player("simo", PlayerColor.BLUE)

  private val italy = State("italy", 3, player1, 0, 0)
  private val france = State("france", 3, player2, 0, 0)
  private val germany = State("germany", 3, player1, 0, 0)
  private val brazil = State("brazil", 5, player1, 0, 0)

  private val europa = Continent("europa", Set("italy", "germany", "france"), 4)

  @Before
  def before(): Unit =
    map.addNode(italy)
    map.addNode(brazil)
    map.addNode(france)
    map.addNode(germany)
    map.addEdge("italy", "france")
    map.addEdge("italy", "germany")
    map.addContinent(europa)

  @Test
  def testPlayerState(): Unit =
    assert(map.playerStates(player1).contains(italy))
    assert(map.playerStates(player1).contains(brazil))

  @Test
  def testGetStateByName(): Unit =
    assertEquals(map.stateByName("italy"), italy)

  @Test
  def testAddContinent(): Unit =
    assert(map.continents.contains(europa))

  @Test
  def testCalcTanksToPlaceWithoutContinent(): Unit =
    map.calcTanksToPlace(player1)
    assertEquals(player1.tanksToPlace,1)

  @Test
  def testCalcTanksToPlaceWithContinent(): Unit =
    map.stateByName("france").setPlayer(player1)
    map.calcTanksToPlace(player1)
    assertEquals(player1.tanksToPlace, 5)

  @Test
  def testAddTanks(): Unit =
    assertEquals(map.stateByName("italy").numberOfTanks, 3)
    map.stateByName("italy").addTanks(1)
    assertEquals(map.stateByName("italy").numberOfTanks, 4)

  @Test
  def testMoveTanks(): Unit =
    assertEquals(map.stateByName("italy").numberOfTanks, 3)
    map.moveTanks("italy", "france", 2)
    assertEquals(map.stateByName("italy").numberOfTanks, 1)
    assertEquals(map.stateByName("france").numberOfTanks, 5)


  @Test
  def testNeighborsOfEnemies(): Unit =
    assert(map.neighborStatesOfEnemies("italy", player1).contains("france"))
    assertFalse(map.neighborStatesOfEnemies("italy", player1).contains("brazil"))
    assertFalse(map.neighborStatesOfEnemies("italy", player1).contains("germany"))

  @Test
  def testNeighborsOfPlayer(): Unit =
    assert(map.neighborStatesOfPlayer("italy", player1).contains("germany"))
    assertFalse(map.neighborStatesOfPlayer("italy", player1).contains("france"))
    assertFalse(map.neighborStatesOfPlayer("italy", player1).contains("brazil"))
