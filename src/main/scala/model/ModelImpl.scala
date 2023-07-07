package model

class ModelImpl() extends Model {

  private var setOfPlayer = Set[Player]()
  override def deployTroops(): Unit = ???

  override def setGameSettings(inputDataPlayer: Set[(String, Int)]): Unit =
    inputDataPlayer.foreach(element =>
      setOfPlayer = setOfPlayer + new PlayerImpl(element._1, Color.fromOrdinal(element._2))
    )

  override def getSetOfPlayers(): Set[Player] = setOfPlayer
}
