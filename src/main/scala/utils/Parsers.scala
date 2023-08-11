package utils

import model.{Continent, State}

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
   * a parser to convert strings to Borders (String, String)
   */
  object BorderParser extends Parser[(String,String)]:
    override def parse(line: String): Option[(String,String)] =
      line.split(",").toList match {
        case state1 :: state2 :: Nil => Some((state1.trim, state2.trim))
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
          Some(Continent(continentName.trim, continentStates, bonus))
        case _ => None
      }