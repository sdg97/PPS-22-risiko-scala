package model

enum PlayerColor(val rgb: Int) {
  case RED extends PlayerColor(0xFF0000)
  case GREEN extends PlayerColor(0x00FF00)
  case BLUE extends PlayerColor(0x0000FF)
  case YELLOW extends PlayerColor(0xFFFF00)
  case BLACK extends PlayerColor(0x000000)
  case PURPLE extends PlayerColor(0x4C0099)
}