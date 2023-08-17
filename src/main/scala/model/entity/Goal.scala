package model.entity

/**
 * The goal for win the game
 */
trait Goal:

  /**
   * @return a goal string description
   */
  def description : String

/**
 * Goal factory
 */
object Goal:

  /**
   * @param description: a goal string description
   * @return new Goal from a string description
   */
  def apply(description: String): Goal = new GoalImpl(description)

  private class GoalImpl(private val _description: String) extends Goal:
    override def description = _description
