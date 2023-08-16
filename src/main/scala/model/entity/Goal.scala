package model.entity

trait Goal:
  def description : String
object Goal:
  def apply(description: String): Goal = new GoalImpl(description)

  private class GoalImpl(private val _description: String) extends Goal:
    override def description = _description
