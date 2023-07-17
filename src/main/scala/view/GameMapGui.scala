package view

import controller.ControllerModule.Controller

import java.awt.{BasicStroke, BorderLayout, Color, Font, Graphics, Graphics2D, Polygon}
import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.geom.{Ellipse2D, Point2D}
import java.io.{File, FileReader}
import javax.swing.{BorderFactory, JButton, JComponent, JFrame, JLabel, JPanel, UIManager}
import scala.collection.mutable
import scala.io.Source
import scala.swing.{Color, Dimension, Font, Image}
import scala.collection.mutable.Map
import scala.math.random
import scala.util.Random


///**
// * import controller.Controller
// * import model.{Model, ModelImpl, PlayerColor, PlayerImpl}
// *
// * import java.awt.{BorderLayout, Color, Font, Graphics, Graphics2D}
//*import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
//*import java.awt.geom.{Ellipse2D, Point2D}
//*import java.io.{File, FileReader}
//*import javax.swing.{BorderFactory, JButton, JFrame, JPanel}
//*import scala.collection.mutable
//*import scala.io.Source
//*import scala.swing.{Dimension, Image}
//*import scala.collection.mutable.Map
//*import javax.swing.UIManager
//*import java.awt.Color
// *
// *
// *object GameMapGui extends App {
// *
// *val model = new ModelImpl()
//  *val controller = new Controller(model)
//  *val buttonMap: mutable.Map[String, JButtonExtended] = mutable.Map()
// *
// *// Carica l'immagine di sfondo
//  *val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/img_map.png"))
// *
// *// Crea il frame principale
//  *val frame = new JFrame("GUI Map")
//  *frame.setLayout(new BorderLayout())
//  *frame.setResizable(false)
// *
// *// Crea il pannello per contenere gli elementi della GUI
//  *val panel = new JPanel(null) {
//    *override def paintComponent(g: Graphics): Unit = {
//      *super.paintComponent(g)
//      *// Disegna l'immagine di sfondo
//      *val widthRatio = getWidth.toDouble / backgroundImage.getWidth(null)
//      *val heightRatio = getHeight.toDouble / backgroundImage.getHeight(null)
//      *val scaleFactor = Math.max(widthRatio, heightRatio)
//      *val scaledWidth = (backgroundImage.getWidth(null) * scaleFactor).toInt
//      *val scaledHeight = (backgroundImage.getHeight(null) * scaleFactor).toInt
//      *val x = (getWidth - scaledWidth) / 2
//      *val y = (getHeight - scaledHeight) / 2
//      *g.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, null)
//    *}
//  *}
//  *panel.setPreferredSize(new Dimension(1000, 650)) // Imposta le dimensioni del pannello
// *
// *val file = new File("src/main/resources/config/states.txt")
//  *val lines = Source.fromFile(file).getLines().toList
// *
// *lines.foreach { line =>
//    *val parts = line.split(",")
//    *if (parts.length >= 3) {
//      *val name = parts(0).trim
//      *val posX = parts(1).trim
//      *val posY = parts(2).trim
// *
// *val btnState = new JButtonExtended("") {
//        *setBorder(BorderFactory.createEmptyBorder())
//        *setContentAreaFilled(false) // Rimuove lo sfondo del bottone
//        *setForeground(Color.BLACK) // Imposta il colore del testo
//        *setFocusPainted(false) // Rimuove l'effetto di focuss
//        *setText(name)
//        *setFont(new Font("Arial", 12, 10))
//        *setRolloverEnabled(true)
// *
// *override def paintComponent(g: Graphics): Unit = {
//          *val g2d = g.asInstanceOf[Graphics2D]
//          *val center = new Point2D.Float(getWidth / 2.0f, getHeight / 2.0f)
//          *val radius = Math.min(getWidth, getHeight) / 2.0f
//          *val circle = new Ellipse2D.Float(center.x - radius, center.y - radius, 2.0f * radius, 2.0f * radius)
//          *g2d.setColor(Color.YELLOW) // Imposta il colore del cerchio
//          *g2d.fill(circle) // Disegna il cerchio
//          *super.paintComponent(g) // Disegna il testo del bottone
//        *}
//      *}
// *
// *btnState.addMouseListener(
//        *new MouseAdapter() {
//        *override def mouseEntered(evt: MouseEvent): Unit = {
//          *if(!btnState.isSelected && !btnState.isNeighbour)
//            *btnState.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
//        *}
// *
// *override def mouseExited(evt: MouseEvent): Unit = {
//          *if(!btnState.isSelected && !btnState.isNeighbour)
//            *btnState.setBorder(BorderFactory.createEmptyBorder())
//        *}
//      *})
// *
// *btnState.addActionListener((_: ActionEvent) => {
//        *if(btnState.isNeighbour) {
//          *//TODO invoke attack method
//          *println("attack")
//          *resetButton()
//        *}
//        *else if(btnState.isSelected) {
//          *resetButton()
//        *}
//        *else {
//          *resetButton()
//          *val neighbors: Set[String] = controller.getNeighbor(name, controller.getCurrentPlayer())
//          *neighbors.foreach(neighbor => {
//            *val currentButton = buttonMap(neighbor)
//            *currentButton.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 2))
//            *currentButton.setIsNeighbour(true)
//          *})
//          *btnState.setSelected(!btnState.isSelected)
//          *btnState.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2))
//        *}
//      *})
// *
// *btnState.setBounds(posX.toInt, posY.toInt, 40, 40)
//      *panel.add(btnState)
//      *buttonMap += (name -> btnState)
//    *}
//  *}
// *
// *val btnShowMyStates = new JButton() {
//    *setText("Show my States")
//  *}
//  *btnShowMyStates.setBounds(400,500,200,50)
//  *panel.add(btnShowMyStates)
//  *btnShowMyStates.addActionListener((_: ActionEvent) => {
//    *println(buttonMap.size)
//    *controller.getPlayerStates(controller.getCurrentPlayer()).foreach(state => buttonMap(state.name).setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2)))
//  *})
// *
// *// Aggiungi il pannello al frame
//  *frame.getContentPane.add(panel)
//  *// Imposta le dimensioni del frame e visualizzalo
//  *frame.pack()
//  *frame.setVisible(true)
// *
// *def resetButton(): Unit =
//    *buttonMap.foreach((_, button) => {
//      *button.setBorder(BorderFactory.createEmptyBorder())
//      *button.setIsNeighbour(false)
//      *button.setSelected(false)
//    *})
//*}
//*/

object GameMapGui:
  private var screen : Option[GameMapGuiImpl] = None
  def apply(c: Controller) =
    screen = Some(new GameMapGuiImpl(c))
    screen.get.screen

  def update() =
    screen.get.update()

private class GameMapGuiImpl(c: Controller):
  val buttonMap: mutable.Map[String, JButtonExtended] = mutable.Map()

  // Carica l'immagine di sfondo
  val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/img_map.png"))


  // Crea il pannello per contenere gli elementi della GUI
  val screen = new JPanel(null) {
    override def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      // Disegna l'immagine di sfondo
      val widthRatio = getWidth.toDouble / backgroundImage.getWidth(null)
      val heightRatio = getHeight.toDouble / backgroundImage.getHeight(null)
      val scaleFactor = Math.max(widthRatio, heightRatio)
      val scaledWidth = (backgroundImage.getWidth(null) * scaleFactor).toInt
      val scaledHeight = (backgroundImage.getHeight(null) * scaleFactor).toInt
      val x = (getWidth - scaledWidth) / 2
      val y = (getHeight - scaledHeight) / 2
      g.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, null)
    }
  }
  screen.setPreferredSize(new Dimension(1000, 650)) // Imposta le dimensioni del pannello

  val panelAttackPhase = new JPanel(null) {
    setBounds(300, 80, 400, 500)
    setBackground(Color.gray)
    setBorder(BorderFactory.createLineBorder(Color.gray, 30))
  };
  screen.add(panelAttackPhase)

  def update(): Unit =
    c.getAllStates().foreach(state => {
      buttonMap(state.name).setText(state.numberOfWagon.toString)
      buttonMap(state.name).setColor(new Color(state.player.color.rgb))

    })

  val labelAttackState = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText("Argentina")
    setFont(new Font("Arial", 12, 17))
  }
  labelAttackState.setBounds(40, 40, 80, 40)
  val labelDefenderState = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText("Brasil")
    setFont(new Font("Arial", 12, 17))
  }
  labelDefenderState.setBounds(300, 40, 80, 40)

  val arrowComponent= new JComponent {
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

  val panelDadoAttack = new JPanel(){
    setBounds(20, 100, 160, 100)
    setBackground(Color.gray)
    setBorder(BorderFactory.createLineBorder(Color.black, 5))
  }

  val panelDadoDefender = new JPanel() {
    setBounds(220, 100, 160, 100)
    setBackground(Color.gray)
    setBorder(BorderFactory.createLineBorder(Color.black, 5))
  }

  val buttonAttack=new JButton(){
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

  buttonAttack.addActionListener((_: ActionEvent) => {
    var i: Int = 1
    while (i <= 3) {
      val dadoAttack = new DadoComponent("attacker")
      val newValue = Random.nextInt(6) + 1
      dadoAttack.setValue(newValue)
      if (i == 1) {
        dadoAttack.setBounds(20, 120, 50, 50)
      }
      else if (i == 2) {
        dadoAttack.setBounds(70, 120, 50, 50)
      }
      else {
        dadoAttack.setBounds(120, 120, 50, 50)
      }
      panelAttackPhase.add(dadoAttack)
      i = i + 1
    }
    buttonDefence.setEnabled(true)
    buttonAttack.setEnabled(false)
  })



  buttonDefence.addActionListener((_: ActionEvent) => {
    var j: Int = 1
    while (j <= 3) {
      val dadoDefender = new DadoComponent("defender")
      val newValue = Random.nextInt(6) + 1
      dadoDefender.setValue(newValue)
      if (j == 1) {
        dadoDefender.setBounds(220, 120, 50, 50)
      }
      else if (j == 2) {
        dadoDefender.setBounds(270, 120, 50, 50)
      }
      else {
        dadoDefender.setBounds(320, 120, 50, 50)
      }
      panelAttackPhase.add(dadoDefender)
      j = j + 1
    }
  })

  panelAttackPhase.add(labelAttackState)
  panelAttackPhase.add(arrowComponent)
  panelAttackPhase.add(labelDefenderState)
  panelAttackPhase.add(panelDadoAttack)
  panelAttackPhase.add(panelDadoDefender)
  panelAttackPhase.add(buttonAttack)
  panelAttackPhase.add(buttonDefence)





