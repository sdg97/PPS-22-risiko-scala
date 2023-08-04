package model

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
  def setGameSettings(inputDataPlayer: Set[(String, String)], typeOfMap:String):MessageSetting
  def getTypeOfMap():VersionMap

object GameSettingManager:
  def apply():GameSettingManager=GameSettingManagerImpl()

  private class GameSettingManagerImpl()extends GameSettingManager:
    private var map:String=""
    override def setGameSettings(inputDataPlayer: Set[(String, String)], typeOfMap:String): MessageSetting = (inputDataPlayer, typeOfMap) match
      case (input, map) if input.exists(_._1 == "") => MessageSetting.ErrorIncompleteUsernames
      case (input, map) if input.exists(element => input.count(_._2 == element._2) > 1) => MessageSetting.ErrorSameColorsSelected
      case (input, map) if input.exists(element => input.count(_._1 == element._1) > 1) => MessageSetting.ErrorDuplicateUsername
      case (input, map) if map.equals("") => MessageSetting.ErrorVersionOfMap
      case _ =>
        map=typeOfMap
        MessageSetting.CorrectSettings

    override def getTypeOfMap(): VersionMap = map match
      case typeMap if typeMap.equals("Classic") => VersionMap.Classic
      case typeMap if typeMap.equals("Europe") => VersionMap.Europe
      case _ => null





