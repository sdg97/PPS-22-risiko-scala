package model

enum MessageSetting:
  case ErrorIncompleteUsernames
  case ErrorDuplicateUsername
  case ErrorSameColorsSelected
  case CorrectSettings

trait GameSettingManager:
  def setGameSettings(inputDataPlayer: Set[(String, String)]):MessageSetting

object GameSettingManager:
  def apply():GameSettingManager=GameSettingManagerImpl()

  private class GameSettingManagerImpl()extends GameSettingManager:
    override def setGameSettings(inputDataPlayer: Set[(String, String)]): MessageSetting = inputDataPlayer match
      case input if input.exists(_._1 == "") => MessageSetting.ErrorIncompleteUsernames
      case input if input.exists(element => input.count(_._2 == element._2) > 1) => MessageSetting.ErrorSameColorsSelected
      case input if input.exists(element => input.count(_._1 == element._1) > 1) => MessageSetting.ErrorDuplicateUsername
      case _ => MessageSetting.CorrectSettings







