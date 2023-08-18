package model.entity

import model.entity.PlayerColor.BLACK
import model.manager.VersionMap
import model.entity.{Player, PlayerColor}


enum PlayerColor(val rgb: Int):
  case RED extends PlayerColor(0xFF0000)
  case GREEN extends PlayerColor(0x00FF00)
  case BLUE extends PlayerColor(0x0000FF)
  case YELLOW extends PlayerColor(0xFFFF00)
  case BLACK extends PlayerColor(0x000000)
  case PURPLE extends PlayerColor(0x4C0099)

trait Player:
  /**
   *
   * @return the name of the Player.
   */
  def username: String

  /**
   *
   * @return the color of the Player.
   */
  def color: PlayerColor

  /**
   *
   * @return the number of tanks to place for the Player.
   */
  def tanksToPlace: Int

  /**
   * method to set the number of tanks to place for the Player.
   *
   * @param tanksNumber the number of tanks to place.
   */
  def setTanksToPlace(tanksNumber: Int): Unit


object Player:
  private class PlayerImpl(_username: String, _color: PlayerColor) extends Player:
    private var _numTanks = 0
    override def username: String = _username
    override def color: PlayerColor = _color
    override def tanksToPlace: Int = _numTanks
    override def setTanksToPlace(tanksNumber: Int): Unit = _numTanks = tanksNumber


  def apply(username: String, color: PlayerColor): Player =
    new PlayerImpl(username, color)

  def apply(username: String): Player =
    new PlayerImpl(username,null)


  extension (players: List[Player])
    def START_TANK_NUMBER(typeOfMap:VersionMap) = typeOfMap match
      case version if version.equals(VersionMap.Classic) => players.size match
        case 3 => 35
        case 4 => 30
        case 5 => 25
        case _ => 20
      case version if version.equals(VersionMap.Europe)=> players.size match
        case 3 => 18
        case 4 => 15
        case 5 => 13
        case _ => 11
        