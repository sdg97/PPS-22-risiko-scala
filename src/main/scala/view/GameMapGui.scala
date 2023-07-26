package view

import controller.ControllerModule.Controller
import view.component.JButtonExtended

import java.awt.{BasicStroke, BorderLayout, Color, FlowLayout, Font, Graphics, Graphics2D, Polygon}
import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.geom.{Ellipse2D, Point2D}
import java.io.{File, FileReader}
import javax.swing.{BorderFactory, JButton, JComponent, JFrame, JLabel, JPanel, SwingConstants, UIManager}
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


  def update(): Unit =
    c.allStates.foreach(state => {
      buttonMap(state.name).setText(state.numberOfTanks.toString)
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

  val panelDadoAttack = new JPanel(null){
    setLayout(new FlowLayout(FlowLayout.CENTER,0, 13))
    setBounds(20, 100, 160, 100)
    setBackground(Color.gray)
    setBorder(BorderFactory.createLineBorder(Color.black, 5))
  }

  val panelDadoDefender = new JPanel() {
    setLayout(new FlowLayout(FlowLayout.CENTER,0, 13))
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

  val random = new Random()
  for (_ <- 1 to 3) {
    val dadoComponentAttack = new DadoComponent("attacker")
    dadoComponentAttack.setPreferredSize(new Dimension(50, 50))
    dadoComponentAttack.setValue(1)
    panelDadoAttack.add(dadoComponentAttack)

    val dadoComponentDefence = new DadoComponent("defender")
    dadoComponentDefence.setPreferredSize(new Dimension(50, 50))
    dadoComponentDefence.setValue(1)
    panelDadoDefender.add(dadoComponentDefence)
  }

  buttonAttack.addActionListener(_=>{
    panelDadoAttack.getComponents.foreach {
      case dadoComponent: DadoComponent =>
        dadoComponent.setValue(random.nextInt(6) + 1)
      case _ =>
    }
    buttonDefence.setEnabled(true)
    buttonAttack.setEnabled(false)
    buttonClose.setEnabled(false)
  })


  buttonDefence.addActionListener((_: ActionEvent) => {
    panelDadoDefender.getComponents.foreach {
      case dadoComponent: DadoComponent =>
        dadoComponent.setValue(random.nextInt(6) + 1)
      case _ =>
    }
    buttonDefence.setEnabled(false)
    buttonAttack.setEnabled(true)
    buttonClose.setEnabled(true)
  })

  val labelWagonAttackState = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText("12")
    setFont(new Font("Arial", 12, 24))
  }
  labelWagonAttackState.setBounds(85, 300, 80, 40)

  val labelWagonDefenderState = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText("10")
    setFont(new Font("Arial", 12, 24))
  }
  labelWagonDefenderState.setBounds(285, 300, 80, 40)

  val labelPlayerMessage = new JLabel() {
    setForeground(Color.BLACK) // Imposta il colore del testo
    setText("Congrats you conquared Argentina")
    setFont(new Font("Arial", 12, 17))
    setHorizontalAlignment(SwingConstants.CENTER)
  }
  labelPlayerMessage.setBounds(20, 410, 300, 80)



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
  screen.add(panelAttackPhase)






