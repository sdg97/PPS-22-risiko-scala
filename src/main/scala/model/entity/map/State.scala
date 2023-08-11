package model.entity.map

import model.entity.map.State
import model.entity.Player

type Position = (Int, Int)

trait State:
  /**
   *
   * @return the name of the state.
   */
  def name: StateName

  /**
   *
   * @return an integer representing the number of tanks in the state.
   */
  def numberOfTanks: Int

  /**
   *
   * @return the player owning the state.
   */
  def player: Player

  /**
   *
   * @return the coordinates of the state in the map.
   */
  def position: Position

  /**
   * method to add tanks to a state.
   * @param numberOfTanksToAdd the number of tanks to add to the state.
   */
  def addTanks(numberOfTanksToAdd: Int): Unit

  /**
   *  method to remove tanks from a state.
   * @param numberOfTanksToRemove the number of tanks to remove from the state.
   */
  def removeTanks(numberOfTanksToRemove: Int): Unit

  /**
   * method to assign state to a player.
   * @param  p the player to assign the status to.
   */
  def setPlayer(p: Player): Unit

object State:

  private class StateImpl(_name: StateName, var _numberOfTanks: Int, var _player: Player, _position: Position) extends State:
    override def name: StateName = _name
    override def numberOfTanks: Int = _numberOfTanks

    override def player: Player = _player

    override def position: Position = _position

    override def setPlayer(p: Player): Unit =
      _player = p

    override def addTanks(numberOfTanksToAdd: Int): Unit =
      _numberOfTanks = _numberOfTanks + numberOfTanksToAdd

    override def removeTanks(numberOfTanksToRemove: Int): Unit =
      _numberOfTanks = _numberOfTanks - numberOfTanksToRemove

  def apply(name: StateName, numberOfTanks: Int, player: Player, position: Position): State =
    new StateImpl(name, numberOfTanks, player, position)

  def apply(name: StateName) : State =
    new StateImpl(name, 0, Player("mock"), (0, 0))

  def apply(name: StateName, numberOfTanks: Int, player: Player): State =
    new StateImpl(name, numberOfTanks, player, (0, 0))