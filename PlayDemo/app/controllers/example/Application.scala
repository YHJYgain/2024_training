package controllers.example

import play.api.mvc._

class Application extends Controller {

  implicit val myCustomCharset: Codec = Codec.javaSupported("iso-8859-1")

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(<h1>Hello World!</h1>).as(HTML)
  }

  def index(name: String): Action[AnyContent] = TODO

  def hello(name: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok("Hello " + name)
  }

  // 重定向到 /application/Bob
  def helloBob(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.Application.hello("Bob"))
  }

  def list(version: Option[String]): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    version match {
      case Some(v) => Ok(s"Version: $v")
      case None => Ok("No version specified")
    }
  }

  // Accept only 10KB of data.
  def save(text: String): Action[String] = Action(parse.text(maxLength = 1024 * 10)) { implicit request: Request[String] =>
    Ok("Got: " + text)
  }

}
