package model

enum PlayerColor(val rgb: Int) {
  case Red extends PlayerColor(0xFF0000)
  case Green extends PlayerColor(0x00FF00)
  case Blue extends PlayerColor(0x0000FF)
  case Yellow extends PlayerColor(0xFFFF00)
  case Black extends PlayerColor(0x000000)
  case Purple extends PlayerColor(0x4C0099)
}