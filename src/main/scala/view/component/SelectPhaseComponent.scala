package view.component

import controller.ControllerModule.Controller
import model.RisikoSwitchPhaseAction
import view.JButtonExtended

import java.awt.event.ActionEvent
import javax.swing.{JButton, JPanel, SwingUtilities}
private[view] class SelectPhaseComponent(private val c: Controller) {
  private val panel = new JPanel()

  def get() =
    println("CHIAMO LA GET")
    update()
    panel

  def update() =
    panel.removeAll()
    panel.revalidate()
    panel.repaint()
    RisikoSwitchPhaseAction.values.foreach(p =>
      val btnState = new JButton(p.toString)
      btnState.setEnabled(c.switchTurnPhaseActionAvailable.contains(p))
      btnState.setSize(btnState.getPreferredSize())
      btnState.addActionListener((_: ActionEvent) => {
        //SwingUtilities.invokeLater(() => c.switchPhase(p))
        c.switchPhase(p)
        c.updateView()
      })
      panel.add(btnState)
    )
    panel.setSize(panel.getPreferredSize())
}