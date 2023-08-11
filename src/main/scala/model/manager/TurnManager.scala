package model.manager

import model.entity.PlayerColor.{BLACK, BLUE, YELLOW}
import model.manager.TurnManager

trait TurnManager[T]:
  def all: Set[T]
  def current: T
  def next(): T

object TurnManager:
  def apply[T](elems: Set[T]): TurnManager[T] = TurnManagerImpl(elems)

  private case class TurnManagerImpl[T](private val toManage: Set[T])
    extends TurnManager[T]:
    private val iterator: Iterator[T] = LazyList.continually(toManage.toList).flatten.iterator
    private var curr :Option[T] = None
    override def next(): T=
      curr = Some(iterator.next())
      curr.get

    override def current: T =
      curr.get

    override def all: Set[T] = toManage







