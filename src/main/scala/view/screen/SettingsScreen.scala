package view.screen


import controller.Controller
import model.manager.SettingResult
import utils.shiftLeft

import java.awt.event.ActionEvent
import java.awt.geom.{Ellipse2D, Point2D, RoundRectangle2D}
import java.awt.{Color, Component, Graphics, GridLayout, RenderingHints}
import javax.swing.JOptionPane.*
import javax.swing.*
import javax.swing.border.Border
import scala.swing.MenuBar.NoMenuBar.listenTo
import scala.swing.event.ButtonClicked
import scala.swing.{Dimension, Font, Graphics2D, Image, Insets, Menu, MenuBar, MenuItem}
import model.entity.Player
import model.entity.map.State

import scala.collection.mutable.ListBuffer

/**
 * Component that allow to show the menu to insert the players’s data and select the type of Risiko’s map
 */
private[view] object SettingsScreen {
  def apply(c: Controller) =

    val backgroundImage: Image = javax.imageio.ImageIO.read(getClass.getResourceAsStream("/img_map.png"))
    val panel = new JPanel(null) {
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
    panel.setPreferredSize(new Dimension(1000, 650)) // Imposta le dimensioni del pannello


    val initialSettingsPanel = new JPanel(null) {
      setBounds(300, 80, 400, 520)
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
    labelNumberOfPlayers.setBounds(70, 40, 120, 40)

    val infoPlayerPanel = new JPanel() {
      setBounds(30, 90, 350, 250)
      setBackground(Color.gray)
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 10))
    }

    val comboBoxVersionMap = new JComboBox[String](Array("", "Classic", "Europe")) {}
    comboBoxVersionMap.setBounds(200, 370, 80, 18)
    var typeOfMap: String = ""

    comboBoxVersionMap.addActionListener((_: ActionEvent) => {
      typeOfMap = comboBoxVersionMap.getSelectedItem().toString
    })

    val comboBoxMenu=new JComboBox[String](Array("3","4","5","6")){}
    comboBoxMenu.setBounds(200, 52, 80,18)
    setPanelInfo(3,infoPlayerPanel)

    comboBoxMenu.addActionListener((_: ActionEvent) => {
      infoPlayerPanel.removeAll()

      val numberOfPlayer = comboBoxMenu.getSelectedItem().toString.toInt
      setPanelInfo(numberOfPlayer,infoPlayerPanel)
      infoPlayerPanel.revalidate()
      infoPlayerPanel.repaint()

    })



    lazy val buttonStart = new JButton() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setFocusPainted(false) // Rimuove l'effetto di focuss
      setText("START")
      setFont(new Font("Arial", 12, 10))
      setBackground(Color.white)
    }
    buttonStart.setBounds(130, 420, 140, 40)

    lazy val labelError = new JLabel() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setText("")
      setFont(new Font("Arial", 12, 14))
    }
    labelError.setBounds(30, 470, 340, 40)

    buttonStart.addActionListener((_) => {
      if (infoPlayerPanel.getComponentCount == 0) {
        labelError.setText("Choose the number of players")
      }
      else {
        val numberOfPlayer = comboBoxMenu.getSelectedItem().toString.toInt
        var inputDataPlayer: ListBuffer[(String, String)] = ListBuffer()
        var i = 1;
        while (i <= numberOfPlayer) {
          inputDataPlayer = inputDataPlayer += ((infoPlayerPanel.getComponents().filter(_.isInstanceOf[JTextField]).map(_.asInstanceOf[JTextField]).find(_.getName.equals("txtFieldPlayer" + i)).get.getText,
            infoPlayerPanel.getComponents().filter(_.isInstanceOf[JComboBox[String]]).map(_.asInstanceOf[JComboBox[String]]).find(_.getName.equals("cmbColor" + i)).get.getSelectedItem.toString))
          i += 1
        }
        val message=c.setGameSettings(inputDataPlayer.toList,typeOfMap)
        if(message.equals(SettingResult.ErrorIncompleteUsernames))
          labelError.setText("All username field must be completed")
        else if(message.equals(SettingResult.ErrorDuplicateUsername))
          labelError.setText("The username must be assigned at only one player")
        else if(message.equals(SettingResult.ErrorSameColorsSelected))
          labelError.setText("The color must be assigned at only one player")
        else if(message.equals(SettingResult.ErrorVersionOfMap))
          labelError.setText("Select the correct version of Map")
        else
          c.showGameView

      }
    })



    def setPanelInfo(numberOfPlayer: Int, panelInfo: JPanel): Unit = {
      panelInfo.setLayout(new GridLayout(numberOfPlayer, numberOfPlayer))
      var i = 1
      var colors = Array("RED", "GREEN", "YELLOW", "PURPLE", "BLUE", "BLACK")
      while (i <= numberOfPlayer) {

        panelInfo.add(new JLabel() {
          setForeground(Color.BLACK) // Imposta il colore del testo
          setText("Player " + i + ": ")
          setFont(new Font("Arial", 12, 13))
        })

        val jtf = new JTextField() {
          setName("txtFieldPlayer" + i)
        }
        jtf.setText(s"Player${i}")

        panelInfo.add(jtf)

        colors = shiftLeft(colors).toArray
        val comboBoxColor = new JComboBox[String](colors) {
          setName("cmbColor" + i)
          setRenderer(new DefaultListCellRenderer {
            override def getListCellRendererComponent(list: JList[_], value: Any, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component = {
              val comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
              value match {
                case "RED" => comp.setBackground(Color.RED)
                case "GREEN" => comp.setBackground(Color.GREEN)
                case "BLUE" => comp.setBackground(Color.BLUE)
                case "PURPLE" => comp.setBackground(new Color(0X8A2BE2))
                case "BLACK" => comp.setBackground(Color.BLACK)
                case "YELLOW" => comp.setBackground(Color.YELLOW)
              }
              setForeground(Color.white)
              comp
            }
          })
        }
        panelInfo.add(comboBoxColor)
        i = i + 1
      }
    }

    val labelVersionMap = new JLabel() {
      setForeground(Color.BLACK) // Imposta il colore del testo
      setText("Select type of map:")
      setFont(new Font("Arial", 12, 14))
    }
    labelVersionMap.setBounds(70, 360, 120, 40)


    initialSettingsPanel.add(labelNumberOfPlayers)
    initialSettingsPanel.add(comboBoxMenu)
    initialSettingsPanel.add(infoPlayerPanel)
    initialSettingsPanel.add(labelVersionMap)
    initialSettingsPanel.add(comboBoxVersionMap)
    initialSettingsPanel.add(buttonStart)
    initialSettingsPanel.add(labelError)
    panel.add(initialSettingsPanel)


    panel


}