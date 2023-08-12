package model.manager

import model.entity.PlayerColor.{BLACK, BLUE, YELLOW}
import model.manager.TurnManager

trait TurnManager[T]:
  def all: List[T]
  def current: T
  def next(): T

object TurnManager:
  def apply[T](elems: List[T]): TurnManager[T] = TurnManagerImpl(elems)

  private case class TurnManagerImpl[T](private val toManage: List[T])
    extends TurnManager[T]:
    private val iterator: Iterator[T] = LazyList.continually(toManage.toList).flatten.iterator
    private var curr :Option[T] = None
    override def next(): T=
      curr = Some(iterator.next())
      curr.get

    override def current: T =
      curr.get

    override def all: List[T] = toManage







