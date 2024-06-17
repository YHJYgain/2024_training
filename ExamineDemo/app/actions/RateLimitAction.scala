package actions

import akka.actor.ActorSystem
import akka.actor.TypedActor.context
import akka.stream.Materializer
import org.slf4j.LoggerFactory
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.{Inject, Singleton}
import scala.concurrent.duration._

/**
 * 限流 Action，用于限制每秒处理的请求数量
 *
 * @param maxRequestsPerSecond 每秒允许处理的最大请求数量
 * @param mat                  Materializer 实例，用于 Akka 流
 * @param ec                   ExecutionContext 实例，用于异步处理
 * @param system               ActorSystem 实例，用于调度任务
 */
@Singleton
class RateLimitAction @Inject()(maxRequestsPerSecond: Int)
                               (implicit val mat: Materializer,
                                ec: ExecutionContext,
                                system: ActorSystem) extends ActionBuilder[Request] {

  private val logger = LoggerFactory.getLogger(classOf[RateLimitAction])
  private val requestCount = new AtomicInteger(0)

  /**
   * 处理传入的请求，并应用限流逻辑
   *
   * @param request 传入的请求
   * @param block   用于处理请求的代码块
   * @tparam A 请求体的类型
   * @return 一个 Future，包含请求处理的结果
   */
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    logger.info(s"收到请求: ${request.uri}")
    if (requestCount.incrementAndGet() <= maxRequestsPerSecond) {
      block(request).map { result =>
        requestCount.decrementAndGet()
        logger.info(s"请求处理完成: ${request.uri}")
        result
      }.recover {
        case ex: Throwable =>
          requestCount.decrementAndGet()
          logger.error(s"处理请求时出错：${request.uri}", ex)
          Results.InternalServerError("服务器内部错误")
      }
    } else {
      requestCount.decrementAndGet()
      logger.warn(s"请求过多：${request.uri}")
      Future.successful(Results.TooManyRequests("请求过多，请稍后再试"))
    }
  }

  /**
   * 重置请求计数
   */
  private def resetRequestCount(): Unit = {
    logger.info("重置请求计数")
    requestCount.set(0)
  }

  // 调度器，每秒运行一次重置请求计数的任务
  system.scheduler.schedule(0.seconds, 1.second)(new Runnable {
    override def run(): Unit = {
      logger.info("调度器正在运行")
      resetRequestCount()
    }
  })(ec)

  override protected def executionContext: ExecutionContext = ec
}