package model

trait Continent:
  def name: String
  def states: Set[String]
  def bonus: Int

object Continent:
  private class ContinentImpl(_name: String, _stateSet: Set[String], _bonus: Int) extends Continent:
    private val _states: Set[String] = _stateSet

    override def name: String = _name

    override def states: Set[String] = _states

    override def bonus: Int = _bonus

  def apply(_name: String, _stateSet: Set[String], _bonus: Int): Continent =
    new ContinentImpl(_name, _stateSet, _bonus)

/**
extension (continent: Set[String])
  def bonus(playerStates: Set[String]): Int =
 *
  */


