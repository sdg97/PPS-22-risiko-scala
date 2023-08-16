package view.component

import controller.Controller
import model.entity.Player

import java.awt.Color
import javax.swing.{JLabel, JPanel}

private[view] class GoalComponent(private val c: Controller):
  private val panel = new JPanel()
  private val description = c.goal.description
  def get() =
    update()
    panel

  def update() =
    val label = JLabel(s"${description}")
    panel.removeAll()
    panel setOpaque false
    panel add label
    panel setOpaque false
    panel setSize panel.getPreferredSize()

