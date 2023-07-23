package model

trait Continent {
  def name: String
  def states: Set[String]
}

class ContinentImpl(_name: String, _stateSet: Set[String]) extends Continent:
  private val _states: Set[String] = _stateSet
  override def name = _name
  override def states: Set[String] = _states



