package controller

import model.*

import view.*
object ControllerModule:
  trait Controller:
    def startNewGame() : Unit
    def setGameSettings(inputDataPlayer: Set[(String, String)], typeOfMap:String) : MessageSetting
    def deployTroops() : Unit
    def neighborStatesOfEnemies(stateName: String): Set[String]
    def neighborStatesOfPlayer(state: String): Set[String]
    def stateByName(stateName: String): State
    def currentPlayerStates: Set[State]
    def currentPlayer: Player
    def allStates: Set[State]
    def updateView(): Unit
    def addTank(stateName: String): Unit
    def tanksToPlace: Int
    def switchTurnPhaseActionAvailable :  Set[RisikoAction]
    def switchPhase(a: RisikoSwitchPhaseAction): Unit
    def resultAttack(): MessageAttackPhase
    def attackPhase(): Unit
    def numberOfDiceForPlayers(attacker: State, defender: State):(Int,Int)
    def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit
    def numberOfTanksToMove(attacker:State):Int
    def currentTurnPhase: RisikoPhase

    def setAttacker(state: State): Unit

    def setDefender(state: State): Unit

    def rollDiceAttacker(): Seq[Int]

    def rollDiceDefender(): Seq[Int]
    def setDefaultAttackSettings:Unit
    
    def getAttacker():State
    def getDefender():State

    def showGameView:Unit
    def setDefaultInitialSettings():Unit
    def setTypeOfMap(): VersionMap

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      def startNewGame() =
        context.view.showSettingsView()
      def setGameSettings(inputDataPlayer: Set[(String, String)], typeOfMap:String) =
        context.model.setGameSettings(inputDataPlayer, typeOfMap)
      
      def deployTroops() =
        model.deployTroops()
        context.view.showGameView()
        view.update()
      override def neighborStatesOfEnemies(stateName: String): Set[String] = model.neighborStatesOfEnemies(stateName)
      override def neighborStatesOfPlayer(state: String): Set[String] = model.neighborStatesOfPlayer(state)
      override def stateByName(stateName: String): State = model.stateByName(stateName)
      override def currentPlayerStates: Set[State] = model.currentPlayerStates
      override def currentPlayer: Player = model.currentPlayer
      override def allStates: Set[State] = model.allStates
      override def updateView(): Unit = view.update()
      override def addTank(stateName: String): Unit =
        model.addTank(stateName)
        view.update()

      override def tanksToPlace: Int = model.tanksToPlace
      override def switchTurnPhaseActionAvailable :  Set[RisikoAction] =
        model.switchTurnPhaseActionAvailable
      override def switchPhase(a: RisikoSwitchPhaseAction): Unit =
        model.switchPhase(a)
        view.update()

      override def resultAttack(): MessageAttackPhase = model.attackResult()

      override def attackPhase(): Unit = model.attack()

      override def numberOfDiceForPlayers(attacker: State, defender: State): (Int, Int) = model.numberOfDiceForPlayers(attacker, defender)

      override def moveTanks(fromStateName: String, toStateName: String, numberOfTanks: Int): Unit =
        model.moveTanks(fromStateName, toStateName, numberOfTanks)
        view.update()

      override def rollDiceAttacker(): Seq[Int] = model.rollDiceAttacker()

      override def rollDiceDefender(): Seq[Int] = model.rollDiceDefender()

      override def numberOfTanksToMove(attacker: State): Int = model.numberOfTanksToMove(attacker)

      override def setAttacker(state: State): Unit = model.setAttacker(state)

      override def setDefender(state: State): Unit = model.setDefender(state)

      override def setDefaultAttackSettings: Unit = model.setDefaultAttackSettings

      override def showGameView: Unit = context.view.showGameView()

      override def setDefaultInitialSettings(): Unit = model.setDefaultInitialSettings()

      override def setTypeOfMap(): VersionMap = model.setTypeOfMap()

      override def getAttacker(): State = model.getAttacker()

      override def getDefender(): State = model.getDefender()

      override def currentTurnPhase: RisikoPhase = model.currentPhase
  trait Interface extends Provider with Component:
    self: Requirements =>
