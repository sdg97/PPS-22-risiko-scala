package model

trait State:
  def name: String
  def numberOfTanks: Int
  def player: Player
  def posX: Int
  def posY: Int
  def addTanks(numberOfTanksToAdd: Int): Unit
  def removeTanks(numberOfTanksToRemove: Int): Unit
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

