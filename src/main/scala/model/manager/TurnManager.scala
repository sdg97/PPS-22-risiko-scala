package model.manager

import model.entity.PlayerColor.{BLACK, BLUE, YELLOW}
import model.manager.TurnManager

/**
 * Player turn alternation manager
 * @tparam T: the players type managed by the TurnManager
 */
trait TurnManager[T]:

  /**
   *
   * @return List of the players managed
   */
  def all: List[T]

  /**
   * the current player
   * @return
   */
  def current: T

  /**
   * Switch to the next player
   * @return the next player
   */
  def next(): T

/**
 * TurnManager factory
 */
object TurnManager:

  /**
   * @param players: List of the players managed
   * @tparam T: the players type managed by the TurnManager
   * @return a new TurnManager manage the list of players in a circular and sequential way
   */
  def apply[T](players: List[T]): TurnManager[T] = TurnManagerImpl(players)

  private case class TurnManagerImpl[T](private val toManage: List[T])
    extends TurnManager[T]:
    private val iterator: Iterator[T] = LazyList.continually(toManage).flatten.iterator
    private var curr :Option[T] = None
    override def next(): T=
      curr = Some(iterator.next())
      curr.get

    override def current: T =
      curr.get

    override def all: List[T] = toManage







