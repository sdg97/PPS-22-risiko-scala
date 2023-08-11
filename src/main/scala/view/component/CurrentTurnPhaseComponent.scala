package view.component

import controller.Controller

import javax.swing.{JLabel, JPanel}

private[view] class CurrentPhaseComponent(c: Controller):
  private val panel = new JPanel()
  private val LABEL_PREFIX = "Phase: "
  def get() =
    update()
    panel

  def update() =
    panel.removeAll()
    panel add JLabel(s"${LABEL_PREFIX} ${c.currentTurnPhase.toString}")
    panel.setSize(panel.getPreferredSize())
