package view.component

import controller.Controller
import model.entity.Player

import java.awt.Color
import javax.swing.{JLabel, JPanel}

private[view] class CurrentPlayerComponent(c: Controller):
  private val panel = new JPanel()
  private val LABEL_PREFIX = "Player:"
  def get() =
    update()
    panel

  def update() =
    val label = JLabel(s"${LABEL_PREFIX} ${c.currentPlayer.getUsername}")
    label.setForeground(Color.decode(c.currentPlayer.getColor.rgb.toString))
    panel.removeAll()
    panel setOpaque false
    panel add label
    panel setOpaque false
    panel setSize panel.getPreferredSize()

