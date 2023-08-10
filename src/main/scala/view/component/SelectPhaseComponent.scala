package view.component

import controller.ControllerModule.Controller
import model.RisikoSwitchPhaseAction.*

import java.awt.event.ActionEvent
import javax.swing.{JButton, JPanel, SwingUtilities}
import scala.+:
import scala.collection.mutable.ListBuffer
private[view] class SelectPhaseComponent(private val c: Controller) {

  private val panel = new JPanel()

  def get() =
    update()
    panel


  def update() =
    panel.removeAll()
    panel.revalidate()
    panel.repaint()
    Array(StartAttack, StartMove, EndTurn).foreach(p =>
      val btnState = new JButton(p.string)
      btnState.setEnabled(c.switchTurnPhaseActionAvailable.contains(p))
      btnState.setSize(btnState.getPreferredSize())
      btnState.addActionListener((_: ActionEvent) => {
        SwingUtilities.invokeLater(() => c.switchPhase(p))
      })
      panel.add(btnState)
    )
    panel.setSize(panel.getPreferredSize())

  def setButtonsEnabled(boolean: Boolean): Unit =
    panel.getComponents.filter(_.isInstanceOf[JButton]).foreach(
      button => button.setEnabled(boolean)
    )
}
