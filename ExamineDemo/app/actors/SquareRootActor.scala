package actors

import akka.actor.{Actor, ActorLogging, Props, ReceiveTimeout}
import akka.pattern.pipe
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.math.sqrt
import scala.util.Try

/**
 * SquareRootActor 负责处理计算平方根的消息
 */
object SquareRootActor {
  /**
   * 创建 SquareRootActor 的 Props
   *
   * @return SquareRootActor 的 Props 实例
   */
  def props: Props = Props[SquareRootActor]

  /**
   * 【低级】计算平方根的消息类
   *
   * @param number 要计算平方根的数字
   */
  case class LowerCalculateSquareRoot(number: Int)

  /**
   * 【高级】计算平方根的消息类
   *
   * @param number 要计算平方根的数字
   */
  case class HigherCalculateSquareRoot(number: Int)
}

/**
 * SquareRootActor 类实现了计算平方根的逻辑
 */
class SquareRootActor extends Actor with ActorLogging {
  import SquareRootActor._

  // 自定义线程池
  implicit val ec: ExecutionContext = context.system.dispatchers.lookup("akka.actor.custom-dispatcher")
  // 自定义超时逻辑：设置接收超时为 3 秒
  context.setReceiveTimeout(3.seconds)

  /**
   * 处理收到的消息
   *
   * @return 接收消息的处理函数
   */
  override def receive: Receive = {
    case LowerCalculateSquareRoot(number) =>
      log.info(s"【低级】正在计算数字 $number 的平方根")
      val result: Future[Either[String, Double]] = Future {
        val delay = scala.util.Random.nextInt(251).toLong
        log.info(s"模拟延迟 $delay 毫秒")
        Thread.sleep(delay)
        checkDelayAndCompute(number, delay)
      }(ec).recover {
        case ex: Exception =>
          log.error(s"计算平方根时出错：${ex.getMessage}")
          Left(ex.getMessage)
      }
      result.pipeTo(sender()).onComplete {
        case scala.util.Success(Right(value)) => log.info(f"计算结果：$value%.2f")
        case scala.util.Success(Left(errorMsg)) => log.error(s"计算失败：$errorMsg")
        case scala.util.Failure(exception) => log.error(s"计算过程中发生异常：${exception.getMessage}")
      }

    case HigherCalculateSquareRoot(number) =>
      log.info(s"【高级】正在计算数字 $number 的平方根")
      val promise = Promise[Either[String, Double]]()
      val delay = scala.util.Random.nextInt(251).milliseconds
      log.info(s"模拟延迟 $delay")

      context.system.scheduler.scheduleOnce(delay) {
        promise.complete {
          Try {
            checkDelayAndCompute(number, delay.toMillis)
          } recover {
            case ex: Exception =>
              log.error(s"计算平方根时出错：${ex.getMessage}")
              Left(ex.getMessage)
          }
        }
      }(context.dispatcher)

      promise.future.pipeTo(sender()).onComplete {
        case scala.util.Success(Right(value)) => log.info(f"异步计算结果：$value%.2f")
        case scala.util.Success(Left(errorMsg)) => log.error(s"异步计算失败：$errorMsg")
        case scala.util.Failure(exception) => log.error(s"异步计算过程中发生异常：${exception.getMessage}")
      }

    case ReceiveTimeout =>
      log.warning("Actor 在超时时间内没有收到消息")
      context.setReceiveTimeout(Duration.Undefined) // 禁用超时
  }

  /**
   * 检查延迟并计算平方根
   *
   * @param number 要计算平方根的数字
   * @param delay  延迟时间（毫秒）
   * @return 包含计算结果或错误信息的 Either 实例
   */
  private def checkDelayAndCompute(number: Int, delay: Long): Either[String, Double] = {
    if (delay < 100) {
      val errorMsg = s"延迟时间小于 100 ms：$delay ms，实现错误"
      Left(errorMsg)
    } else if (delay > 200) {
      val errorMsg = s"延迟时间大于 200 ms：$delay ms，请求失败"
      Left(errorMsg)
    } else {
      val result = f"${sqrt(number)}%.2f".toDouble
      Right(result)
    }
  }
}
