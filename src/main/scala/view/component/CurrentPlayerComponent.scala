package view.component

import controller.ControllerModule.Controller
import model.Player

import java.awt.Color
import javax.swing.{JLabel, JPanel}

private[view] class CurrentPlayerComponent(c: Controller):
  private val panel = new JPanel()
  private val LABEL_PREFIX = "Player:"
  def get() =
    update()
    panel

  def update() =
    val label = JLabel(s"${LABEL_PREFIX} ${c.getCurrentPlayer().username}")
    label.setForeground(Color.decode(c.getCurrentPlayer().color.rgb.toString))
    panel.removeAll()
    panel setOpaque false
    panel add label
    panel setOpaque false
    panel setSize panel.getPreferredSize()
