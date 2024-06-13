package controllers

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
class HomeController extends Controller {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val anyStatus = Status(488)("Strange response type")
    Ok(views.html.index())
  }

  def helloWorld(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok("Hello, World!")
  }

  def helloWorldAsync(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val futureResult: Future[String] = Future {
      // Simulate a long-running task
      Thread.sleep(3000)
      "Hello, World!"
    }
    futureResult.map { result =>
      Ok(result)
    }
  }

}
