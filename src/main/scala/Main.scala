import controller.*
import model.*
import view.View

/** Entry point for the application */
object Main extends App {
  View.start()
  val m = new ModelImpl()
  val c = new Controller(m)
  c.start()
}