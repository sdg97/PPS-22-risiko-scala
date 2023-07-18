package view.component

import controller.ControllerModule.Controller
import model.Player

import javax.swing.{JLabel, JPanel}

private[view] class CurrentPlayerComponent(c: Controller):
  private val panel = new JPanel()
  private val LABEL_PREFIX = "Player"
  def get() =
    update()
    panel

  def update() =
    panel.removeAll()
    panel add JLabel(s"${LABEL_PREFIX} ${c.getCurrentPlayer().username}")
    panel.setSize(panel.getPreferredSize())
    