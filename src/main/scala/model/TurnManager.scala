package model

import model.PlayerColor.{BLACK, BLUE, YELLOW}

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

object TurnManagerTest extends App:
  val turnManager : TurnManager[Player] = TurnManager(Set(Player("simone", BLUE),
    Player("Martin", BLACK),
    Player("Pietro", YELLOW)))

  println(turnManager.next().username)
  println(turnManager.next().username)
  println(turnManager.next().username)
  println(turnManager.next().username)
  println(turnManager.next().username)






