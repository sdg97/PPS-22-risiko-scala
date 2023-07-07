import controller.*
import model.*
import view.View

/** Entry point for the application */
object Main extends App {
  View.start()
  val m = new Model()
  val c = new Controller(m)
  c.start()
}