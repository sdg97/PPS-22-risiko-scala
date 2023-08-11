package model.config

import model.entity.map
import model.entity.map.{Continent, State, StateName}

object Parsers:
  /**
   * Allows to parse a string to an object of type T
   *
   * @tparam T the parser's return type
   */
  trait Parser[T]:
    /**
     * convert the given string in an object of type T if possible
     *
     * @param line the string to be converted
     * @return an option containing the result if the parsing went well, an empty option otherwise
     */
    def parse(line: String): Option[T]

  /**
   * a parser to convert strings to State
   */
  object StateParser extends Parser[State] :
    override def parse(line: String): Option[State] =
      line.split(",").toList match {
        case name :: posX :: posY :: Nil => Some(State(name.trim, 0, null, (posX.trim.toInt, posY.trim.toInt)))
        case _ => None
      }

  /**
   * a parser to convert strings to Borders (StateName, StateName)
   */
  object BorderParser extends Parser[(StateName, StateName)]:
    override def parse(line: String): Option[(StateName, StateName)] =
      line.split(",").toList match {
        case stateName1 :: stateName2 :: Nil => Some((stateName1.trim, stateName2.trim))
        case _ => None
      }

  /**
   * a parser to convert strings to Continent
   */
  object ContinentParser extends Parser[Continent]:
    override def parse(line: String): Option[Continent] =
      line.split(",").toList match {
        case continentName :: other if other.nonEmpty =>
          val continentStates = other.dropRight(1).map(_.trim).toSet
          val bonus = other.last.trim.toInt
          Some(map.Continent(continentName.trim, continentStates, bonus))
        case _ => None
      }