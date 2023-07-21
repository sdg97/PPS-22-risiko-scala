package utils

import model.{StateImpl}
import model.State

import java.io.File
import scala.io.Source


def shiftLeft[T](a: IndexedSeq[T]): IndexedSeq[T] =
  val b = a.tail
  b.appended(a.head)

def statesFromFile(file: String): Set[State] =
  val stateFile = new File(file)
  val stateFileLines: Seq[String] = Source.fromFile(stateFile).getLines().toList
  var s: List[State] = List()
  stateFileLines.map( line =>
    val parts = line.split(",")
    if (parts.length >= 3) {
      val name = parts(0).trim
      val posX = parts(1).trim
      val posY = parts(2).trim
      s = s.appended(State(name))
    }
  )
  s.toSet
object TryRingBuffer extends App:
  val colors = Array("RED", "GREEN", "YELLOW", "PURPLE", "BLUE", "BLACK")
  println(shiftLeft(colors))

