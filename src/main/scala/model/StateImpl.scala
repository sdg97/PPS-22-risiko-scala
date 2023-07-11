package model

class StateImpl (_name: String, var _numberOfWagon: Int, _player: Player) extends State {
  override def name: String = _name

  override def numberOfWagon: Int = _numberOfWagon

  override def player: Player = _player

  override def addWagon(numberOfWagonsToAdd: Int): Unit =
    _numberOfWagon = _numberOfWagon+numberOfWagonsToAdd

  override def removeWagon(numberOfWagonToRemove: Int): Unit =
    _numberOfWagon= _numberOfWagon-numberOfWagonToRemove

  def this(_name:String)={
    this(_name,0,null)
  }
  
  


}
