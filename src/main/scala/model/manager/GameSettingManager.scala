package model.manager

import model.manager.{GameSettingManager, MessageSetting}

enum MessageSetting:
  case ErrorIncompleteUsernames
  case ErrorDuplicateUsername
  case ErrorSameColorsSelected
  case ErrorVersionOfMap
  case CorrectSettings
  
enum VersionMap:
  case Europe
  case Classic

trait GameSettingManager:
  type PlayerSettings= List[(String, String)]
  type TypeMap= String
  def setGameSettings(inputDataPlayer: PlayerSettings, typeOfMap:TypeMap):MessageSetting
  def getTypeOfMap():VersionMap

object GameSettingManager:
  def apply():GameSettingManager=GameSettingManagerImpl()

  private class GameSettingManagerImpl()extends GameSettingManager:
    private var map:TypeMap=""
    override def setGameSettings(inputDataPlayer: PlayerSettings, typeOfMap:TypeMap): MessageSetting = (inputDataPlayer, typeOfMap) match
      case _ if inputDataPlayer.exists(_._1 == "") => MessageSetting.ErrorIncompleteUsernames
      case _ if inputDataPlayer.exists(element => inputDataPlayer.count(_._2 == element._2) > 1) => MessageSetting.ErrorSameColorsSelected
      case _ if inputDataPlayer.exists(element => inputDataPlayer.count(_._1 == element._1) > 1) => MessageSetting.ErrorDuplicateUsername
      case _ if map.equals("") => MessageSetting.ErrorVersionOfMap
      case _ =>
        map=typeOfMap
        MessageSetting.CorrectSettings

    override def getTypeOfMap(): VersionMap = map match
      case "Classic" => VersionMap.Classic
      case "Europe" => VersionMap.Europe
      case _ => null





