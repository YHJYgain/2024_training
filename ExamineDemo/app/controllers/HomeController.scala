package controllers

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class HomeController @Inject()(actorSystem: ActorSystem) extends Controller {

  implicit val timeout: Timeout = 5.seconds // 自定义超时逻辑

  def sqrtLow(input: Int): Action[AnyContent] = Action.async {
    val actor = actorSystem.actorSelection("/user/sqrtActorLow")
    (actor ? input).mapTo[Double].map { result =>
      Ok(result.toString)
    }
  }

  def sqrtHigh(input: Int): Action[AnyContent] = Action.async {
    val actor = actorSystem.actorSelection("/user/sqrtActorHigh")
    (actor ? input).mapTo[Double].map { result =>
      Ok(result.toString)
    }
  }
}
