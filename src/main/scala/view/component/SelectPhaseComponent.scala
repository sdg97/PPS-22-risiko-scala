package view.component

import controller.Controller
import model.manager.RisikoSwitchPhaseAction.*

import java.awt.event.ActionEvent
import javax.swing.{JButton, JPanel, SwingUtilities}
import scala.+:
import scala.collection.mutable.ListBuffer

/**
 * Component that allow to select the turn phase
 * @param controller
 */
private[view] class SelectPhaseComponent(private val controller: Controller) {

  private val panel = new JPanel()

  /**
   *
   * @return the component
   */
  def get() =
    update()
    panel

  /**
   * update the component with updated data
   */
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

  /**
   * Enabled or dabled the components button
   * @param boolean
   */
  def setButtonsEnabled(boolean: Boolean): Unit =
    panel.getComponents.filter(_.isInstanceOf[JButton]).foreach(
      button => button.setEnabled(boolean)
    )
}
