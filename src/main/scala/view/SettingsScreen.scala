package view
import controller.ControllerModule.*
import model.Player

import java.awt.event.ActionEvent
import java.awt.geom.{Ellipse2D, Point2D}
import java.awt.{Color, Graphics, GridLayout}
import javax.swing.JOptionPane.*
import javax.swing.*
import scala.swing.MenuBar.NoMenuBar.listenTo
import scala.swing.event.ButtonClicked
import scala.swing.{Dimension, Font, Graphics2D, Image, Menu, MenuBar, MenuItem}

/**
 * View for setup simulation.
 */
private[view] object SettingsScreen {
  def apply(c: Controller) = {
    val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/map_grey.jpg"))
    val panel = new JPanel(null) {
      /**
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
      }*/
    }
    panel.setPreferredSize(new Dimension(1000, 650)) // Imposta le dimensioni del pannello

    val panelInfoPlayer=new JPanel(null){
      setBounds(450,100,400,500)
      setBackground(Color.gray)
      setBorder(BorderFactory.createLineBorder(Color.gray, 30))
    };

    val labelNumberOfPlayers = new JLabel() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setText("Number of players: ")
      setFont(new Font("Arial", 12, 13))
      override def paintComponent(g: Graphics): Unit = {
        g.setColor(Color.white)
        g.fillRect(130, 150, 140, 40)
        super.paintComponent(g)
      }
    }

    val panelInfo = new JPanel() {
      setBounds(30, 90, 350, 250)

      setBackground(Color.gray)
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 10))
    }

    val comboBoxMenu=new JComboBox[String](Array("3","4","5","6"))
    comboBoxMenu.setBounds(200, 52, 80,18)
    comboBoxMenu.addActionListener((_: ActionEvent) => {
      panelInfo.removeAll()

      val numberOfPlayer=comboBoxMenu.getSelectedItem().toString.toInt
      panelInfo.setLayout(new GridLayout(numberOfPlayer, numberOfPlayer))
      var i = 1
      while(i<=numberOfPlayer){

        panelInfo.add(new JLabel() {
          setForeground(Color.BLACK) // Imposta il colore del testo
          setText("Player "+i+": ")
          setFont(new Font("Arial", 12, 13))
        })
        panelInfo.add(new JTextField() {
          setName("txtFieldPlayer" + i)
        })
        val comboBoxColor = new JComboBox[String](Array("RED", "GREEN", "YELLOW", "PURPLE", "BLUE", "BLACK")) {
          setName("cmbColor" + i)
        }
        panelInfo.add(comboBoxColor)
        i=i+1
      }
    })

    val buttonStart= new JButton(){
      setContentAreaFilled(false) // Rimuove lo sfondo del bottone
      setForeground(Color.WHITE) // Imposta il colore del testo
      setFocusPainted(false) // Rimuove l'effetto di focuss
      setText("START")
      setFont(new Font("Arial", 12, 10))
      setBackground(Color.BLACK)
    }
    buttonStart.setBounds(130, 370, 140, 40)

    val labelError = new JLabel() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setText("")
      setFont(new Font("Arial", 12, 13))
    }
    labelError.setBounds(30, 420, 340, 40)

    buttonStart.addActionListener((_) => {
      if (panelInfo.getComponentCount == 0) {
        labelError.setText("Choose the number of players")
      }
      else{
        val numberOfPlayer=comboBoxMenu.getSelectedItem().toString.toInt
        var inputDataPlayer: Set[(String, String)]=Set()
        var i=1;
        while(i<=numberOfPlayer){
          if(panelInfo.getComponents().filter(_.isInstanceOf[JTextField]).map(_.asInstanceOf[JTextField]).find(_.getName.equals("txtFieldPlayer"+i)).get.getText.equals("")){
            labelError.setText("All username field must be completed")
            i=numberOfPlayer;
          }
          else if(inputDataPlayer.exists(_._2==panelInfo.getComponents().filter(_.isInstanceOf[JComboBox[String]]).map(_.asInstanceOf[JComboBox[String]]).find(_.getName.equals("cmbColor"+i)).get.getSelectedItem.toString)) {
            labelError.setText("A color must be assigned at only one player")
            i = numberOfPlayer;
          }
          else{
            inputDataPlayer = inputDataPlayer + ((panelInfo.getComponents().filter(_.isInstanceOf[JTextField]).map(_.asInstanceOf[JTextField]).find(_.getName.equals("txtFieldPlayer"+i)).get.getText,
              panelInfo.getComponents().filter(_.isInstanceOf[JComboBox[String]]).map(_.asInstanceOf[JComboBox[String]]).find(_.getName.equals("cmbColor"+i)).get.getSelectedItem.toString))
          }
          i=i+1
        }
        if(inputDataPlayer.size.equals(3)){
          //labelError.setText(inputDataPlayer.head._1+", "+inputDataPlayer.head._2)
          c.setGameSettings(inputDataPlayer)
//          val m = new ModelImpl()
//          labelError.setText(m.getSetOfPlayers().head.toString)
        }

      }
    })

    val restartButton = new JButton() {

      setContentAreaFilled(false) // Rimuove lo sfondo del bottone
      setForeground(Color.BLACK) // Imposta il colore del testo
      setFocusPainted(false) // Rimuove l'effetto di focuss
      setText("Restart")
      setFont(new Font("Arial", 12, 10))

      override def paintComponent(g: Graphics): Unit = {
        val g2d = g.asInstanceOf[Graphics2D]
        val center = new Point2D.Float(getWidth / 2.0f, getHeight / 2.0f)
        val radius = Math.min(getWidth, getHeight) / 2.0f
        val circle = new Ellipse2D.Float(center.x - radius, center.y - radius, 2.0f * radius, 2.0f * radius)
        g2d.setColor(Color.YELLOW) // Imposta il colore del cerchio
        g2d.fill(circle) // Disegna il cerchio
        super.paintComponent(g) // Disegna il testo del bottone
      }
    }

    val playerNumbersLabel = for i <- 2 to 6 yield s"Players${i}"

    val cb = new JComboBox[String](playerNumbersLabel.toArray)
    cb.setBounds(159, 81, 189, 41)


    /**
    panelInfoPlayer.add(labelNumberOfPlayers)
    panelInfoPlayer.add(comboBoxMenu)
    panelInfoPlayer.add(panelInfo)
    panelInfoPlayer.add(buttonStart)
    panelInfoPlayer.add(labelError)
    //panelInfoPlayer.add(restartButton)
    panel.add(panelInfoPlayer)
    */

    panel.add(restartButton)
    panel.add(cb)

    panel


//    val button1 = new JButton() {
//      //setBorder(BorderFactory.createEmptyBorder())
//      setContentAreaFilled(false) // Rimuove lo sfondo del bottone
//      setForeground(Color.BLACK) // Imposta il colore del testo
//      setFocusPainted(false) // Rimuove l'effetto di focuss
//      setText(9.toString)
//      setFont(new Font("Arial", 12, 10))
//
//      override def paintComponent(g: Graphics): Unit = {
//        val g2d = g.asInstanceOf[Graphics2D]
//        val center = new Point2D.Float(getWidth / 2.0f, getHeight / 2.0f)
//        val radius = Math.min(getWidth, getHeight) / 2.0f
//        val circle = new Ellipse2D.Float(center.x - radius, center.y - radius, 2.0f * radius, 2.0f * radius)
//        g2d.setColor(Color.YELLOW) // Imposta il colore del cerchio
//        g2d.fill(circle) // Disegna il cerchio
//        super.paintComponent(g) // Disegna il testo del bottone
//      }
//    }
//    button1.setBounds(38, 78, 40, 40)
//    button1.addActionListener((_: ActionEvent) => {
//      val players = ("Martin", 0xFF0000) :: ("Pietro", 0x00FF00) :: ("Simone", 0x0000FF) :: Nil
//      val mapIndex = 1
//      //c.setGameSettings()
//    })
//    panel add button1

  }

  /**
   * Interessante converte le stringhe in intero, utile per non doverlo specificare tutte
   * le volte che prendo in ingresso qualcosa dall'interfaccia grafica
   * occhio che in scala 3 non si fanno così gli impliciti xD
  private implicit def numberFrom(component: JTextField): Int = component.getText toInt

  private implicit def numberFrom[T](component: JComboBox[T]): Int = component.getSelectedItem.toString toInt
   */
}
