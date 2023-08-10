package view.component
import controller.ControllerModule.Controller
import view.GameScreenImpl

import javax.swing.*
import java.awt.*
import java.awt.event.*

class MovePhasePanel(gameScreen:GameScreenImpl, c: Controller, fromState: String, toState: String) {
  val frame = new JFrame("Wagon to shift")
  frame.setSize(300, 200)
  frame.setLayout(new BorderLayout())
  frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)

  gameScreen.disableMouse()

  private val mainContainer = new JPanel()
  mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS))
  mainContainer.setBackground(Color.DARK_GRAY)

  private val titleLabel = new JLabel("Numero di carri armati da spostare: ")
  titleLabel.setHorizontalAlignment(SwingConstants.CENTER)
  private val headerPanel = new JPanel()
  headerPanel.add(titleLabel)

  private var number = 1
  private val numberLabel = new JLabel(number.toString)
  numberLabel.setHorizontalAlignment(SwingConstants.CENTER)

  private val plusButton = new JButton("+")
  plusButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      if(number < c.stateByName(fromState).numberOfTanks-1)
        number += 1
        numberLabel.setText(number.toString)
    }
  })

  private val minusButton = new JButton("-")
  minusButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      if(number > 0)
        number -= 1
        numberLabel.setText(number.toString)
    }
  })

  private val mainPanel = new JPanel()
  mainPanel.setLayout(new FlowLayout())
  mainPanel.add(minusButton)
  mainPanel.add(numberLabel)
  mainPanel.add(plusButton)

  private val confirmButton = new JButton("Conferma")
  confirmButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      c.moveTanks(fromState, toState, number)
      gameScreen.enableMouse()
      frame.dispose()
    }
  })

  private val confirmPanel = new JPanel(new FlowLayout())
  confirmPanel.add(confirmButton)

  mainContainer.add(headerPanel)
  mainContainer.add(mainPanel)
  mainContainer.add(confirmPanel)

  frame.add(mainContainer, BorderLayout.CENTER)
  frame.setLocationRelativeTo(null)
  frame.setVisible(true)
}
