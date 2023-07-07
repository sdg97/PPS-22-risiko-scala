package view

/**
 * Parameters to start the game
 * @param players list of player names and colors
 * @param map selected for the current game
 */
case class GameSettings(players: List[(String,Int)], map: Int)
