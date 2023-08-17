package view.component

import controller.Controller
import model.entity.Player

import java.awt.Color
import javax.swing.{JLabel, JPanel}

/**
 * Component that show the player that are playing
 * @param controller
 */
private[view] class CurrentPlayerComponent(private val controller: Controller):
  private val panel = new JPanel()
  private val LABEL_PREFIX = "Player:"

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
    val label = JLabel(s"${LABEL_PREFIX} ${c.currentPlayer.username}")
    label.setForeground(Color.decode(c.currentPlayer.color.rgb.toString))
    panel.removeAll()
    panel setOpaque false
    panel add label
    panel setOpaque false
    panel setSize panel.getPreferredSize()

