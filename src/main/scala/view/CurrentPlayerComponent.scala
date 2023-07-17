package view

import model.Player

import javax.swing.{JLabel, JPanel}

private[view] class CurrentPlayerComponent:
  private val panel = new JPanel()
  private val LABEL_PREFIX = "Player"
  def get(p: Player) =
    panel add JLabel(s"${LABEL_PREFIX} ${p.username}")
    panel.setSize(panel.getPreferredSize())
    panel

  def update(p: Player) = 
    panel.removeAll()
    panel add JLabel(s"${LABEL_PREFIX} ${p.username}")
    panel.setSize(panel.getPreferredSize())
    