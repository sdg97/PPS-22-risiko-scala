package view.component

import controller.ControllerModule.Controller
import model.RisikoSwitchPhaseAction
import view.JButtonExtended

import javax.swing.JPanel
private[view] class SelectPhaseComponent(private val c: Controller) {
  private val panel = new JPanel()

  def get() =
    println("CHIAMO LA GET")
    update()
    panel

  def update() =
    panel.removeAll()
    RisikoSwitchPhaseAction.values.foreach(p =>
      val btnState = new JButtonExtended(p.toString)
      btnState.setEnabled(c.switchTurnPhaseActionAvailable.contains(p))
      btnState.setSize(btnState.getPreferredSize())
      panel.add(btnState)
    )
    panel.setSize(panel.getPreferredSize())
}
