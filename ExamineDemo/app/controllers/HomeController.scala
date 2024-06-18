package controllers

import actors.SquareRootActor
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import play.api.mvc._
import actions.RateLimitAction

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._


/**
 * HomeController 负责处理与平方根计算相关的 HTTP 请求
 *
 * @param actorSystem     Akka ActorSystem，用于创建和管理 Actor
 * @param rateLimitAction RateLimitAction 用于限制每秒处理的请求数量
 * @param ec              ExecutionContext 实例，用于异步操作
 */
@Singleton
class HomeController @Inject()(actorSystem: ActorSystem, rateLimitAction: RateLimitAction)
                              (implicit val ec: ExecutionContext) extends Controller {

  // 负责计算平方根的 Akka Actor 实例
  private val squareRootActor = actorSystem.actorOf(SquareRootActor.props, "squareRootActor")
  // 隐式超时设置，用于 ask 模式请求的超时时间
  implicit val timeout: Timeout = 5.seconds

  /**
   * 【低级】计算给定整数的平方根
   *
   * @param number 输入的整数
   * @return 返回平方根的 HTTP 响应
   */
  def lowerSqrt(number: Int): Action[AnyContent] = rateLimitAction.async {
    (squareRootActor ? SquareRootActor.LowerCalculateSquareRoot(number)).mapTo[Either[String, Double]].map {
      case Right(result) => Ok(f"$result%.2f")
      case Left(errorMsg) => InternalServerError(errorMsg)
    }
  }

  /**
   * 【高级】计算给定整数的平方根
   *
   * @param number 输入的整数
   * @return 返回平方根的 HTTP 响应
   */
  def higherSqrt(number: Int): Action[AnyContent] = rateLimitAction.async {
    (squareRootActor ? SquareRootActor.HigherCalculateSquareRoot(number)).mapTo[Either[String, Double]].map {
      case Right(result) => Ok(f"$result%.2f")
      case Left(errorMsg) => InternalServerError(errorMsg)
    }
  }
}
