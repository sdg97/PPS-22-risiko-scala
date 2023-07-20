package view.component

import controller.ControllerModule.Controller
import model.RisikoSwitchPhaseAction
import view.JButtonExtended

import java.awt.event.ActionEvent
import javax.swing.{JPanel, SwingUtilities}
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
      println(c.switchTurnPhaseActionAvailable)
      btnState.setEnabled(c.switchTurnPhaseActionAvailable.contains(p))
      btnState.setSize(btnState.getPreferredSize())
      btnState.addActionListener((_: ActionEvent) => {
        SwingUtilities.invokeLater(() => c.switchPhase(p))
      })
      panel.add(btnState)
    )
    panel.setSize(panel.getPreferredSize())
}
