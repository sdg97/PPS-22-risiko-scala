package view.window

import controller.Controller
import model.entity.Player
import model.entity.map.State
import model.manager.attackmanager.MessageAttackPhase
import view.component.{DiceComponent, JButtonExtended, SelectPhaseComponent}
import view.screen.GameScreen

import java.awt.*
import java.awt.event.ActionEvent
import java.util.Random
import javax.swing.*
import scala.collection.mutable.ArrayBuffer
import scala.swing.MenuBar.NoMenuBar.{publish, reactions}
import scala.swing.event.{ButtonClicked, WindowClosed, WindowClosing}
import scala.swing.{Dialog, Dimension, Frame, Graphics2D}

case class MyButtonClickEvent(button: JButton) extends scala.swing.event.Event

/**
 * Component that allow the players to do an attack/defence and visualize the result of this
 * @param gameScreen
 * @param controller an istance of controller to interoperate diractly with it
 */
class GameWindowAttack(gameScreen:GameScreen,controller: Controller) {

  val frame = new JFrame("Wagon to shift")
  frame.setSize(420, 520)
  frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)


  private val stateAttack=controller.attackerState()
  private val stateDefender=controller.defenderState()

  gameScreen.setClickable(false)

  val panelAttackPhase = new JPanel(null) {
    setBounds(1, 1, 420, 520)
    setBackground(Color.gray)
  };


  val labelAttackState = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText(stateAttack.name)
    setFont(new Font("Arial", 12, 16))
  }
  labelAttackState.setBounds(12, 40, 160, 40)
  val labelDefenderState = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText(stateDefender.name)
    setFont(new Font("Arial", 12, 16))
  }
  labelDefenderState.setBounds(220, 40, 160, 40)

  val arrowComponent = new JComponent {
    override def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)

      val g2d = g.asInstanceOf[Graphics2D]

      // Imposta il colore e lo spessore della linea
      g2d.setColor(Color.BLACK)
      g2d.setStroke(new BasicStroke(2))

      // Calcola le coordinate dei punti della freccia
      val x1 = 50
      val y1 = getHeight / 2
      val x2 = getWidth - 50
      val y2 = y1
      val x3 = x2 - 15
      val y3 = y1 - 10
      val x4 = x2 - 15
      val y4 = y1 + 10

      // Disegna la linea principale


      // Disegna la punta della freccia come un poligono
      val arrowHead = new Polygon()
      arrowHead.addPoint(x3, y3)
      arrowHead.addPoint(x2, y2)
      arrowHead.addPoint(x4, y4)
      g2d.fillPolygon(arrowHead)
    }

    setFont(new Font("Arial", 12, 17))
  }
  arrowComponent.setBounds(180, 40, 80, 40)

  val panelDadoAttack = new JPanel(null) {
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 13))
    setBounds(20, 100, 160, 100)
    setBackground(Color.gray)
    setBorder(BorderFactory.createLineBorder(Color.black, 5))
  }

  val panelDadoDefender = new JPanel() {
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 13))
    setBounds(220, 100, 160, 100)
    setBackground(Color.gray)
    setBorder(BorderFactory.createLineBorder(Color.black, 5))
  }

  val buttonClose = new JButton() {
    setForeground(Color.BLACK)
    setBackground(Color.WHITE)
    setText("CLOSE")
    setFont(new Font("Arial", 12, 13))
    setBounds(140, 350, 120, 50)
    setEnabled(false)
  }

  val buttonAttack = new JButton() {
    setForeground(Color.BLACK)
    setBackground(Color.WHITE)
    setText("ATTACK")
    setFont(new Font("Arial", 12, 13))
    setBounds(40, 220, 120, 50)
  }

  val buttonDefence = new JButton() {
    setForeground(Color.BLACK)
    setBackground(Color.WHITE)
    setText("DEFENCE")
    setFont(new Font("Arial", 12, 13))
    setBounds(240, 220, 120, 50)
    setEnabled(false)
  }


  showAttackPlayerDice(panelDadoAttack)
  showDefenderPlayerDice(panelDadoDefender)

  buttonAttack.addActionListener(_ => {
    controller.executeAttack()
    val rollDiceAttack = controller.rollDiceAttacker()
    showAttackPlayerDice(panelDadoAttack)

    for (i <- 0 to rollDiceAttack.size - 1) {
      val component = panelDadoAttack.getComponent(i).asInstanceOf[DiceComponent]
      component.setValue(rollDiceAttack(i))

    }
    panelDadoAttack.revalidate()
    panelDadoAttack.repaint()
    buttonDefence.setEnabled(true)
    buttonAttack.setEnabled(false)
    buttonClose.setEnabled(false)
  })


  buttonDefence.addActionListener(_ => {
    val rollDiceDefender = controller.rollDiceDefender()

    showDefenderPlayerDice(panelDadoDefender)
    for (i <- 0 to rollDiceDefender.size-1 ) {
      val component = panelDadoDefender.getComponent(i).asInstanceOf[DiceComponent]
      component.setValue(rollDiceDefender(i))
    }
    panelDadoDefender.revalidate()
    panelDadoDefender.repaint()

    labelWagonAttackState.setText(stateAttack.numberOfTanks.toString)
    labelWagonDefenderState.setText(stateDefender.numberOfTanks.toString)

    if(controller.resultOfAttack().equals(MessageAttackPhase.ConqueredState)){
      labelPlayerMessage.setText("""<html>Great, you conquered <br>""" + stateDefender.name)
      controller.updateView()
      panelAttackPhase.remove(buttonAttack)
      panelAttackPhase.remove(buttonDefence)
      buttonClose.setBounds(140, 220, 120, 50)
      labelPlayerMessage.setBounds(40, 340, 300, 80)
      val numberOfTanks=controller.numberOfTanksToMove()
      if (numberOfTanks.size==1) {
        controller.moveTanks(stateAttack.name, stateDefender.name, numberOfTanks.head)
        controller.updateView()
        buttonClose.setEnabled(true)
      }
      else {
        showNumberOfTanksToMove(panelAttackPhase, buttonClose, stateAttack.name, stateDefender.name, numberOfTanks)
        controller.updateView()
      }
    }
    else if (controller.resultOfAttack().equals(MessageAttackPhase.LoseAttack)) {
      labelPlayerMessage.setText("""<html>Sorry, but you can't attack <br>because you have only one tank <br> in """ + stateDefender.name + """</html>""".stripMargin)
      controller.updateView()
      panelAttackPhase.remove(buttonAttack)
      panelAttackPhase.remove(buttonDefence)
      buttonClose.setBounds(140, 220, 120, 50)
      labelPlayerMessage.setBounds(40, 340, 300, 80)
      buttonClose.setEnabled(true)
    }
    else if(controller.resultOfAttack().equals(MessageAttackPhase.Winner)){
      labelPlayerMessage.setText("""<html>Congratulation """+stateAttack.player.username+""",<br>you are the WINNER!!! </html>""")
      controller.updateView()
      panelAttackPhase.remove(buttonAttack)
      panelAttackPhase.remove(buttonDefence)
      buttonClose.setBounds(140, 220, 120, 50)
      labelPlayerMessage.setBounds(40, 340, 300, 80)
      buttonClose.setEnabled(true)
      buttonClose.addActionListener(_ => {
        controller.setDefaultInitialSettings()
        frame.setVisible(false)
        controller.startNewGame()
      })
    }
    else {
      buttonDefence.setEnabled(false)
      buttonAttack.setEnabled(true)
      buttonClose.setEnabled(true)
      controller.updateView()
    }

  })

  buttonClose.addActionListener(_ => {
    controller.setDefaultAttackSettings
    frame.setVisible(false)
    gameScreen.setClickable(true)
  })

  lazy val labelWagonAttackState = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText(stateAttack.numberOfTanks.toString)
    setFont(new Font("Arial", 12, 24))
  }
  labelWagonAttackState.setBounds(85, 300, 80, 40)

  lazy val labelWagonDefenderState = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText(stateDefender.numberOfTanks.toString)
    setFont(new Font("Arial", 12, 24))
  }
  labelWagonDefenderState.setBounds(285, 300, 80, 40)

  lazy val labelPlayerMessage = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText("")
    setFont(new Font("Arial", 12, 17))
    setHorizontalAlignment(SwingConstants.CENTER)
  }
  labelPlayerMessage.setBounds(40, 410, 300, 80)


  panelAttackPhase.add(labelAttackState)
  panelAttackPhase.add(arrowComponent)
  panelAttackPhase.add(labelDefenderState)
  panelAttackPhase.add(panelDadoAttack)
  panelAttackPhase.add(panelDadoDefender)
  panelAttackPhase.add(buttonAttack)
  panelAttackPhase.add(buttonDefence)
  panelAttackPhase.add(labelWagonAttackState)
  panelAttackPhase.add(labelWagonDefenderState)
  panelAttackPhase.add(buttonClose)
  panelAttackPhase.add(labelPlayerMessage)

  frame.add(panelAttackPhase, BorderLayout.CENTER)
  frame.setLocationRelativeTo(null)
  frame.setVisible(true)

  /**
   * Method that allow to show the panel for visualize the attacker’s dice and it's values
   *
   * @param panelDiceAttack
   */
  def showAttackPlayerDice(panelDiceAttack:JPanel):Unit={
    panelDiceAttack.removeAll()
    panelDiceAttack.revalidate()
    panelDiceAttack.repaint()
    var numberOfDice=controller.numberOfDiceAttacker()
    if(controller.rollDiceAttacker()==null)
      numberOfDice=controller.numberOfDiceAttacker()
    else
      numberOfDice =controller.rollDiceAttacker().size
    for (_ <- 1 to numberOfDice) {
      val diceComponentAttack = new DiceComponent("attacker")
      diceComponentAttack.setPreferredSize(new Dimension(50, 50))
      diceComponentAttack.setValue(1)
      panelDiceAttack.add(diceComponentAttack)
    }


  }

  /**
   * Method that allow to show the panel for visualize the defender’s dice and it's values
   *
   * @param panelDiceDefender
   */
  def showDefenderPlayerDice(panelDiceDefender:JPanel):Unit={
    panelDiceDefender.removeAll()
    panelDiceDefender.revalidate()
    panelDiceDefender.repaint()
    var numberOfDice = controller.numberOfDiceDefender()
    if (controller.rollDiceDefender() == null)
      numberOfDice = controller.numberOfDiceDefender()
    else
      numberOfDice = controller.rollDiceDefender().size
    for (_ <- 1 to numberOfDice) {
      val diceComponentDefence = new DiceComponent("defender")
      diceComponentDefence.setPreferredSize(new Dimension(50, 50))
      diceComponentDefence.setValue(1)
      panelDiceDefender.add(diceComponentDefence)
    }
  }

  /**
   * Method that allow to select the number of tanks to move in a conquered state
   *
   * @param panel
   * @param buttonClose
   * @param stateAttack
   * @param stateDefender
   * @param numberOfTanks
   */
  def showNumberOfTanksToMove(panel: JPanel, buttonClose:JButton, stateAttack:String, stateDefender: String, numberOfTanks:Seq[Int]):Unit={
    val labelNumberOfTanksToMove=new JLabel(){
      setText("""<html>Select the number of tank to move, in the conquared state </html>""")
      setFont(new Font("Arial", 12, 17))
      setForeground(Color.BLACK)
      setBounds(20, 410, 250, 80)
    }

    val arrayOfTankToMove: ArrayBuffer[String]= ArrayBuffer()
    arrayOfTankToMove+=""
    numberOfTanks.foreach{element=>
      arrayOfTankToMove+=element.toString
    }

    val comboBoxMenu=new JComboBox[String](arrayOfTankToMove.toArray){
      setBounds(300, 430, 80,38)
    }


    comboBoxMenu.addActionListener((_: ActionEvent) => {
      if(!comboBoxMenu.getSelectedItem().toString.equals("")){
        buttonClose.setEnabled(true)

        val numberOfTank = comboBoxMenu.getSelectedItem().toString.toInt
        controller.moveTanks(stateAttack, stateDefender, numberOfTank)
        comboBoxMenu.setEnabled(false)
      }

    })

    panel.add(labelNumberOfTanksToMove)
    panel.add(comboBoxMenu)
  }





}
