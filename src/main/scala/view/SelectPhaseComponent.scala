package view

import javax.swing.JPanel
import controller.ControllerModule.Controller
private[view] class SelectPhaseComponent(private val c: Controller) {
  private val panel = new JPanel()

  def get() =
    println("CHIAMO LA GET")
    update()
    panel

  def update() =
    panel.removeAll()
    val nextPhases = c.nextTurnPhases

    c.turnPhases.foreach(p =>

      val btnState = new JButtonExtended(p.toString)
      btnState.setEnabled(nextPhases.contains(p))
      btnState.setSize(btnState.getPreferredSize())
      panel.add(btnState)
    )
    panel.setSize(panel.getPreferredSize())
}
