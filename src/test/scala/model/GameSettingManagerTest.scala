package model

import model.manager.{GameSettingManager, SettingResult, VersionMap}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class GameSettingManagerTest extends AnyFunSpec with Matchers:
  describe("Game Settings Manager"){
    val manager=GameSettingManager()
    it("should have a way to verify if there is one or more username empty"){
      assert(manager.setGameSettings(List(("Player1","RED"),("","YELLOW"),("Player3","GREEN")),"Classic").equals(SettingResult.ErrorIncompleteUsernames))
    }
    it("should have a way to verify if there is one or more username duplicated") {
      assert(manager.setGameSettings(List(("Player1","RED"),("Player1","YELLOW"),("Player3","GREEN")),"Classic").equals(SettingResult.ErrorDuplicateUsername))
    }
    it("should have a way to verify if a color was assigned to multiple users") {
      assert(manager.setGameSettings(List(("Player1","RED"),("Player2","RED"),("Player3","GREEN")),"Classic").equals(SettingResult.ErrorSameColorsSelected))
    }
    it("should have a way to verify if the version of map was not selected"){
      assert(manager.setGameSettings(List(("Player1","RED"),("Player2","YELLOW"),("Player3","GREEN")),"").equals(SettingResult.ErrorVersionOfMap))
    }
    it("should have a way to set the initial settings of game"){
      assert(manager.setGameSettings(List(("Player1","RED"),("Player2","YELLOW"),("Player3","GREEN")),"Classic").equals(SettingResult.CorrectSettings))
    }
    it("should have a way to get the type of map"){
      assert(manager.typeOfMap().equals(VersionMap.Classic))
    }
  }
