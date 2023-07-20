package model
import controller.ControllerModule
import view.ViewModule.Requirements

import scala.util.Random


object ModelModule:
  trait Model {

    @throws(classOf[MyCustomException])
    def setGameSettings(inputDataPlayer: Set[(String, String)]): Unit

    def getSetOfPlayers(): Set[Player]

    def deployTroops(): Unit

    def getNeighbor(stateName: String, player: Player): Set[String]

    def getPlayerStates(player: Player): Set[State]
    def getAllStates: Set[State]

    def getCurrentPlayer(): Player

    def updateView(): Unit

    def resultAttack(attackerState: State, defenderState: State): Unit
    @throws(classOf[MyCustomException])
    def attackPhase(attackerState: State, defenderState: State): Unit

    def addWagon(stateName: String): Unit

    def rollDice(typeOfPlayer:String, state:State): Seq[Int]

    def numberOfDiceForPlayers(attackerState: State, defenderState: State):(Int,Int)
    

  }

  type Requirements = ControllerModule.Provider

  trait Provider:
    val model: Model

  trait Component:
    context: Requirements =>

    import java.io.File
    import scala.io.Source

    class ModelImpl extends Model:
      val gameMap = new GameMap()
      val player1 = new PlayerImpl("pie", PlayerColor.YELLOW)
      val player2 = new PlayerImpl("martin", PlayerColor.BLUE)
      val player3 = new PlayerImpl("simo", PlayerColor.RED)

      val stateFile = new File("src/main/resources/config/states.txt")
      val stateFileLines: Seq[String] = Source.fromFile(stateFile).getLines().toList

      stateFileLines.foreach { line =>
        val parts = line.split(",")
        if (parts.length >= 3) {
          val name = parts(0).trim
          parts(1).trim
          parts(2).trim
          if(name.equals("alaska"))
            gameMap.addNode(new StateImpl(name, 4, player1))
          else
            gameMap.addNode(new StateImpl(name,2,player2))
        }
      }

      val borderFile = new File("src/main/resources/config/borders.txt")
      val borderFileLines: Seq[String] = Source.fromFile(borderFile).getLines().toList
      borderFileLines.foreach { line =>
        val parts = line.split(",")
        if (parts.length >= 2) {
          val state1 = parts(0).trim
          val state2 = parts(1).trim
          gameMap.addEdge(state1, state2)
        }
      }

      override def getNeighbor(stateName: String, player: Player): Set[String] = gameMap.getNeighborStates(stateName, player)

      override def getPlayerStates(player: Player): Set[State] =
        controller.updateView()
        gameMap.getPlayerStates(player)

      override def getCurrentPlayer(): Player = player1

      private var setOfPlayer = Set[Player]()
      override def setGameSettings(inputDataPlayer: Set[(String, String)]): Unit = {
        if (inputDataPlayer.exists(_._1 == "")) {
          throw new MyCustomException("All username field must be completed")
        }
        else if (inputDataPlayer.exists(element => inputDataPlayer.count(_._2 == element._2) > 1)) {
          throw new MyCustomException("A color must be assigned at only one player")
        }
        else if (inputDataPlayer.exists(element => inputDataPlayer.count(_._1 == element._1) > 1)) {
          throw new MyCustomException("A username must be assigned at only one player")
        }
        else {
          inputDataPlayer.foreach(element =>
            setOfPlayer = setOfPlayer + new PlayerImpl(element._1, PlayerColor.valueOf(element._2))
          )
          gameMap.assignStatesToPlayers(setOfPlayer)
        }
      }
      override def deployTroops(): Unit = println("troop deployed")

      override def getSetOfPlayers(): Set[Player] = setOfPlayer

      override def getAllStates: Set[State] = gameMap.nodes

      override def resultAttack(attackerState: State, defenderState: State): Unit =
        var wagonlostAttacker: Int = 0;
        var wagonlostDefender: Int = 0;
        rollDiceAttack.zip(rollDiceDefender).map { case (elem1, elem2) =>
          if (elem1 > elem2)
            wagonlostDefender = wagonlostDefender + 1
          else if (elem1 <= elem2)
            wagonlostAttacker = wagonlostAttacker + 1
        }
        attackerState.removeWagon(wagonlostAttacker)
        defenderState.removeWagon(wagonlostDefender)

      override def attackPhase(attackerState: State, defenderState: State): Unit = {
        if (attackerState.numberOfWagon > 1 && defenderState.numberOfWagon == 0) {
          defenderState.setPlayer(attackerState.player)
          throw new MyCustomException("""<html>Great, you conquered <br>""" + defenderState.name)
        }
        else if (attackerState.numberOfWagon == 1) {
          throw new MyCustomException("""<html>Sorry, but you can't attack <br>because you have only one wagon <br> in """ + defenderState.name+"""</html>""".stripMargin)
        }
      }

      override def updateView(): Unit = controller.updateView()

      private var rollDiceAttack=Seq[Int]()
      private var rollDiceDefender=Seq[Int]()
      override def rollDice(typeOfPlayer: String, state: State): Seq[Int] = {
        var numberOfDice: Int = 0;
        var resultRollDice=Seq[Int]()
        if(typeOfPlayer.equals("attack")){
          if (state.numberOfWagon > 3) {
            numberOfDice = 3;
          } else {
            numberOfDice = state.numberOfWagon - 1;
          }
          resultRollDice=Seq.fill(numberOfDice)(Random.nextInt(6) + 1).sorted.reverse
          rollDiceAttack=resultRollDice
        }
        else{
          if (state.numberOfWagon >= 3) {
            numberOfDice = 3;
          } else {
            numberOfDice = state.numberOfWagon;
          }
          resultRollDice = Seq.fill(numberOfDice)(Random.nextInt(6) + 1).sorted.reverse
          rollDiceDefender = resultRollDice
        }
        resultRollDice
      }

      override def numberOfDiceForPlayers(attackerState: State, defenderState: State): (Int, Int) = {
        var numberOfDiceAttack=0
        var numberOfDiceDefender=0
        if (attackerState.numberOfWagon > 3) {
          numberOfDiceAttack = 3;
        } else {
          numberOfDiceAttack = attackerState.numberOfWagon - 1;
        }

        if (defenderState.numberOfWagon >= 3) {
          numberOfDiceDefender = 3;
        } else {
          numberOfDiceDefender = defenderState.numberOfWagon;
        }
        (numberOfDiceAttack,numberOfDiceDefender)
      }

      override def addWagon(stateName: String): Unit =
        gameMap.getStateByName(stateName).addWagon(1)
        controller.updateView()

  trait Interface extends Provider with Component:
    self: Requirements =>

