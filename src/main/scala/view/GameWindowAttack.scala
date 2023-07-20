package view

import controller.ControllerModule.Controller
import model.{MyCustomException, Player, State}

import java.awt.{BasicStroke, Color, FlowLayout, Font, Graphics, Polygon}
import java.util.Random
import javax.swing.{BorderFactory, JButton, JComponent, JLabel, JPanel, SwingConstants}
import scala.swing.{Dimension, Graphics2D}

class GameWindowAttack( panelAttackPhase:JPanel, gameScreen: GameScreenImpl, controller: Controller, stateAttack: State, stateDefender:State) {
  def show():Unit={
    val labelAttackState = new JLabel() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setText(stateAttack.name)
      setFont(new Font("Arial", 12, 17))
    }
    labelAttackState.setBounds(20, 40, 160, 40)
    val labelDefenderState = new JLabel() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setText(stateDefender.name)
      setFont(new Font("Arial", 12, 17))
    }
    labelDefenderState.setBounds(230, 40, 160, 40)

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
      val rollDiceAttack=controller.rollDice("attack", stateAttack)
      showAttackPlayerDice(panelDadoAttack)

      for(i<-0 to rollDiceAttack.size-1){
        val component=panelDadoAttack.getComponent(i).asInstanceOf[DadoComponent]
        component.setValue(rollDiceAttack(i))

      }
      panelDadoAttack.revalidate()
      panelDadoAttack.repaint()
      buttonDefence.setEnabled(true)
      buttonAttack.setEnabled(false)
      buttonClose.setEnabled(false)
    })


    buttonDefence.addActionListener(_=> {
      val rollDiceDefender = controller.rollDice("defender", stateDefender)

      showDefenderPlayerDice(panelDadoDefender)
      for (i <- 0 to rollDiceDefender.size - 1) {
        val component = panelDadoDefender.getComponent(i).asInstanceOf[DadoComponent]
        component.setValue(rollDiceDefender(i))
      }
      panelDadoDefender.revalidate()
      panelDadoDefender.repaint()

      controller.resultAttack(stateAttack,stateDefender)
      labelWagonAttackState.setText(stateAttack.numberOfWagon.toString)
      labelWagonDefenderState.setText(stateDefender.numberOfWagon.toString)

      try {
        controller.attackPhase(stateAttack,stateDefender)
      } catch {
        case e: MyCustomException =>
          labelPlayerMessage.setText(e.getMessage)
      }

      if(labelPlayerMessage.getText.equals("Great, you conquered " + stateDefender.name)){
        controller.updateView()
        buttonDefence.setEnabled(false)
        buttonAttack.setEnabled(false)
        buttonClose.setEnabled(true)
      }
      else if(labelPlayerMessage.getText.equals("Sorry, but you can't attack because you have only one wagon in " + stateDefender.name)){
        controller.updateView()
        buttonDefence.setEnabled(false)
        buttonAttack.setEnabled(false)
        buttonClose.setEnabled(true)
      }
      else{
        buttonDefence.setEnabled(false)
        buttonAttack.setEnabled(true)
        buttonClose.setEnabled(true)
      }

    })

    buttonClose.addActionListener(_ => {
      val parent=panelAttackPhase.getParent
      parent.remove(panelAttackPhase)
      parent.revalidate()
      parent.repaint()
    })

    lazy val labelWagonAttackState = new JLabel() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setText(stateAttack.numberOfWagon.toString)
      setFont(new Font("Arial", 12, 24))
    }
    labelWagonAttackState.setBounds(85, 300, 80, 40)

    lazy val labelWagonDefenderState = new JLabel() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setText(stateDefender.numberOfWagon.toString)
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
  }

  def showAttackPlayerDice(panelDadoAttack:JPanel):Unit={
    panelDadoAttack.removeAll()
    panelDadoAttack.revalidate()
    panelDadoAttack.repaint()
    val numberOfDice = controller.numberOfDiceForPlayers(stateAttack, stateDefender)
    for (_ <- 1 to numberOfDice._1) {
      val dadoComponentAttack = new DadoComponent("attacker")
      dadoComponentAttack.setPreferredSize(new Dimension(50, 50))
      dadoComponentAttack.setValue(1)
      panelDadoAttack.add(dadoComponentAttack)
    }


  }

  def showDefenderPlayerDice(panelDadoDefender:JPanel):Unit={
    panelDadoDefender.removeAll()
    panelDadoDefender.revalidate()
    panelDadoDefender.repaint()
    val numberOfDice = controller.numberOfDiceForPlayers(stateAttack, stateDefender)
    for (_ <- 1 to numberOfDice._2) {
      val dadoComponentDefence = new DadoComponent("defender")
      dadoComponentDefence.setPreferredSize(new Dimension(50, 50))
      dadoComponentDefence.setValue(1)
      panelDadoDefender.add(dadoComponentDefence)
    }
  }



}
