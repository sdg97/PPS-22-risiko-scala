package view.component

import model.VersionMap

import java.awt.event.{MouseEvent, MouseListener}
import java.awt.{Graphics, LayoutManager}
import javax.swing.{JPanel, SwingUtilities}
import scala.swing.{Dimension, Image}

class JPanelScreen(layoutManager: LayoutManager, versionMap:VersionMap) extends JPanel() {

  private def setImage():String = versionMap match {
    case VersionMap.Europe => "src/main/resources/img_map_europe.png"
    case VersionMap.Classic => "src/main/resources/img_map.png"
  }
  val backgroundImage: Image = javax.imageio.ImageIO.read(new java.io.File(setImage()))
  setLayout(layoutManager)
  
  
  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val widthRatio = getWidth.toDouble / backgroundImage.getWidth(null)
    val heightRatio = getHeight.toDouble / backgroundImage.getHeight(null)
    val scaleFactor = Math.max(widthRatio, heightRatio)
    val scaledWidth = (backgroundImage.getWidth(null) * scaleFactor).toInt
    val scaledHeight = (backgroundImage.getHeight(null) * scaleFactor).toInt
    val x = (getWidth - scaledWidth) / 2
    val y = (getHeight - scaledHeight) / 2
    g.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, null)
  }
  setPreferredSize(new Dimension(1000, 650))
}
