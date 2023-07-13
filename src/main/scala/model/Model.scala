package model


object ModelModule:
  trait Model {

    @throws(classOf[MyCustomException])
    def setGameSettings(inputDataPlayer: Set[(String, String)]): Unit

    def getSetOfPlayers(): Set[Player]

    def deployTroops(): Unit

    def getNeighbor(stateName: String, player: Player): Set[String]

    def getPlayerStates(player: Player): Set[State]

    def getCurrentPlayer(): Player
    
  }

  trait Provider:
    val model: Model

  trait Component:
    import model.ModelImpl

  trait Interface extends Provider with Component


