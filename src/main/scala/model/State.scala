package model

trait State {
  def name: String
  def numberOfWagon: Int
  def player: Player
  def posX: Int
  def posY: Int
  def addWagon(numberOfWagonsToAdd: Int): Unit
  def removeWagon(numberOfWagonToRemove: Int): Unit
  def setPlayer(p: Player): Unit
}

object State:
  private class StateImpl(_name: String, var _numberOfWagon: Int, var _player: Player, _posX: Int, _posY: Int) extends State {
    override def name: String = _name
    override def numberOfWagon: Int = _numberOfWagon

    override def player: Player = _player

    override def posX: Int = _posX

    override def posY: Int = _posY

    override def setPlayer(p: Player): Unit =
      _player = p

    override def addWagon(numberOfWagonsToAdd: Int): Unit =
      _numberOfWagon = _numberOfWagon + numberOfWagonsToAdd

    override def removeWagon(numberOfWagonToRemove: Int): Unit =
      _numberOfWagon = _numberOfWagon - numberOfWagonToRemove

    def this(_name: String) = {
      this(_name, 0, null, 0, 0)
    }
  }

  def apply(name: String, numberOfWagon: Int, player: Player, posX: Int, posY: Int): State =
    new StateImpl(name, numberOfWagon, player, posX, posY)

  //da modificare o da togliere
  def apply(name: String) : State =
    new StateImpl(name, 0, Player("mock"), 0, 0)

