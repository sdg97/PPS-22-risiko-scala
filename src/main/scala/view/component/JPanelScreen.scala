package view.component

import java.awt.event.{MouseEvent, MouseListener}
import java.awt.{Graphics, LayoutManager}
import javax.swing.{JPanel, SwingUtilities}
import scala.swing.{Dimension, Image}

class JPanelScreen(layoutManager: LayoutManager) extends JPanel() {
  //val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/img_map.png"))
  val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File("src/main/resources/img_map_europe.png"))
  setLayout(layoutManager)
  
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
  setPreferredSize(new Dimension(1000, 650)) // Imposta le dimensioni del pannello

}
