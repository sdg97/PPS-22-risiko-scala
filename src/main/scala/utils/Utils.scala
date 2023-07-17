package utils


def shiftLeft[T](a: IndexedSeq[T]): IndexedSeq[T] =
  val b = a.tail
  b.appended(a.head)
object TryRingBuffer extends App:
  val colors = Array("RED", "GREEN", "YELLOW", "PURPLE", "BLUE", "BLACK")
  println(shiftLeft(colors))

