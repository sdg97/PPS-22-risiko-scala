package model.manager

import model.manager.{GameSettingManager, SettingResult}

enum SettingResult:
  case ErrorIncompleteUsernames
  case ErrorDuplicateUsername
  case ErrorSameColorsSelected
  case ErrorVersionOfMap
  case CorrectSettings
  
enum VersionMap:
  case Europe
  case Classic

type PlayerSettings = List[(String, String)]
type TypeMap = String
trait GameSettingManager:
  def setGameSettings(inputDataPlayer: PlayerSettings, typeOfMap:TypeMap):SettingResult
  def typeOfMap():VersionMap

object GameSettingManager:
  def apply():GameSettingManager=GameSettingManagerImpl()

  private class GameSettingManagerImpl()extends GameSettingManager:
    private var map:TypeMap=""
    override def setGameSettings(inputDataPlayer: PlayerSettings, typeOfMap:TypeMap): SettingResult = (inputDataPlayer, typeOfMap) match
      case _ if inputDataPlayer.exists(_._1 == "") => SettingResult.ErrorIncompleteUsernames
      case _ if inputDataPlayer.exists(element => inputDataPlayer.count(_._2 == element._2) > 1) => SettingResult.ErrorSameColorsSelected
      case _ if inputDataPlayer.exists(element => inputDataPlayer.count(_._1 == element._1) > 1) => SettingResult.ErrorDuplicateUsername
      case _ if typeOfMap.equals("") => SettingResult.ErrorVersionOfMap
      case _ =>
        map=typeOfMap
        SettingResult.CorrectSettings

    override def typeOfMap(): VersionMap = map match
      case "Classic" => VersionMap.Classic
      case "Europe" => VersionMap.Europe
      case _ => null





