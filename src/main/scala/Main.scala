import controller.*
import model.*
import view.*
object MVC
  extends ModelModule.Interface
    with ViewModule.Interface
    with ControllerModule.Interface:

  // Instantiation of components, dependencies are implicit
  override val model = new ModelImpl()
  override val view = new ViewImpl()
  override val controller = new ControllerImpl()

object Main extends App:
  MVC.view.show()