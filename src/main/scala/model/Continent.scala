package model

trait Continent:
  /**
   * @return the name of the continent.
   */
  def name: String

  /**
   * @return the Set of all the names of the states that make up the continent.
   */
  def states: Set[StateName]

  /**
   * @return an integer representing the number of bonus tanks you get if you own all states on the continent.
   */
  def bonus: Int

object Continent:
  private class ContinentImpl(_name: String, _stateSet: Set[StateName], _bonus: Int) extends Continent:
    private val _states: Set[StateName] = _stateSet
    override def name: String = _name
    override def states: Set[StateName] = _states
    override def bonus: Int = _bonus

  def apply(_name: String, _stateSet: Set[StateName], _bonus: Int): Continent =
    new ContinentImpl(_name, _stateSet, _bonus)

