package view

import javax.swing.JPanel
import controller.ControllerModule.Controller
private[view] class SelectPhaseComponent(private val c: Controller) {
  private val panel = new JPanel()

  def get() =
    update()
    panel

  def update() =
    panel.removeAll()
    val nextPhases = c.nextTurnPhases
    println("disegno le fasi del turno")
    c.turnPhases.foreach(p =>
      println("disegno i bottoni")
      val btnState = new JButtonExtended(p.toString)
      btnState.setEnabled(nextPhases.contains(p))
    )
    panel.setSize(panel.getPreferredSize())
}
