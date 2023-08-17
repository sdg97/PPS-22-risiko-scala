package view.component

import controller.Controller
import javax.swing.{JLabel, JPanel}

/**
 * Component that show the current turn phase
 * @param controller
 */
private[view] class CurrentPhaseComponent(private val controller: Controller):
  private val panel = new JPanel()
  private val LABEL_PREFIX = "Phase: "


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
    panel add JLabel(s"${LABEL_PREFIX} ${controller.currentTurnPhase.toString}")
    panel.setSize(panel.getPreferredSize())
