package model

import model.PlayerColor.{BLACK, BLUE, YELLOW}

trait TurnManager[+T]:
  def next(): T

object TurnManager:

  def apply[T](elems: T*): TurnManager[T] = TurnManagerImpl(elems)

  private case class TurnManagerImpl[+T](private val iterable: Iterable[T])
    extends TurnManager[T]:
    private val iterator: Iterator[T] = LazyList.continually(iterable.toList).flatten.iterator

    override def next(): T=
      iterator.next()

object TurnManagerTest extends App:
  val turnManager : TurnManager[Player] = TurnManager(Player("simone", BLUE),
    Player("Martin", BLACK),
    Player("Pietro", YELLOW))

  println(turnManager.next().username)
  println(turnManager.next().username)
  println(turnManager.next().username)
  println(turnManager.next().username)
  println(turnManager.next().username)






