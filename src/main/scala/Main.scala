import controller.*
import model.*
import view.*

/**
 * Object that represent the MVC architecural pattern.
 * Mix the Model, View and Controller module
 */
object MVC
  extends ModelModule.Interface
    with ViewModule.Interface
    with ControllerModule.Interface:

  // Instantiation of components, dependencies are implicit
  override val model = new ModelImpl()
  override val view = new ViewImpl()
  override val controller = new ControllerImpl()

/**
 * The application starting point
 */
object Main extends App:
  MVC.view.show()