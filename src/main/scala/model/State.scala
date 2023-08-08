package model

trait State:
  /**
   *
   * @return the name of the state.
   */
  def name: String

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
   * @return the x-coordinate of the state in the map.
   */
  def posX: Int

  /**
   *
   * @return the y-coordinate of the state in the map.
   */
  def posY: Int

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
  private class StateImpl(_name: String, var _numberOfTanks: Int, var _player: Player, _posX: Int, _posY: Int) extends State:
    override def name: String = _name
    override def numberOfTanks: Int = _numberOfTanks

    override def player: Player = _player

    override def posX: Int = _posX

    override def posY: Int = _posY

    override def setPlayer(p: Player): Unit =
      _player = p

    override def addTanks(numberOfTanksToAdd: Int): Unit =
      _numberOfTanks = _numberOfTanks + numberOfTanksToAdd

    override def removeTanks(numberOfTanksToRemove: Int): Unit =
      _numberOfTanks = _numberOfTanks - numberOfTanksToRemove

  def apply(name: String, numberOfTanks: Int, player: Player, posX: Int, posY: Int): State =
    new StateImpl(name, numberOfTanks, player, posX, posY)

  //da modificare o da togliere
  def apply(name: String) : State =
    new StateImpl(name, 0, Player("mock"), 0, 0)

