package view.component

import controller.Controller
import model.entity.Player

import java.awt.Color
import javax.swing.{JLabel, JPanel}

/**
 * Component that show the goal for win the game
 * @param controller
 */
private[view] class GoalComponent(private val controller: Controller):
  private val panel = new JPanel()
  private val description = c.goal.description

  /**
   * @return the component
   */
  def get() =
    update()
    panel

  /**
   * update the component with updated data
   */
  def update() =
    val label = JLabel(s"${description}")
    panel.removeAll()
    panel setOpaque false
    panel add label
    panel setOpaque false
    panel setSize panel.getPreferredSize()

