package model

trait Continent:
  /**
   * @return the name of the continent.
   */
  def name: String

  /**
   * @return the Set of all the names of the states that make up the continent.
   */
  def states: Set[String]

  /**
   * @return an integer representing the number of bonus tanks you get if you own all states on the continent.
   */
  def bonus: Int

object Continent:
  private class ContinentImpl(_name: String, _stateSet: Set[String], _bonus: Int) extends Continent:
    private val _states: Set[String] = _stateSet
    override def name: String = _name
    override def states: Set[String] = _states
    override def bonus: Int = _bonus

  def apply(_name: String, _stateSet: Set[String], _bonus: Int): Continent =
    new ContinentImpl(_name, _stateSet, _bonus)

