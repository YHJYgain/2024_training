package actors

import akka.actor.{Actor, Props}

import java.util.concurrent.{LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class SqrtActorLow extends Actor {
  // 自定义线程池，使用固定数量的线程和有界队列
  private val threadPool: ThreadPoolExecutor = new ThreadPoolExecutor(
    10, // corePoolSize 核心线程数
    10, // maximumPoolSize 最大线程数
    0L, // keepAliveTime 保活时间
    TimeUnit.MILLISECONDS, // 时间单位
    new LinkedBlockingQueue // 有界队列
  )

  override def receive: Receive = {
    case input: Int =>
      val originalSender = sender()
      threadPool.execute(new Runnable {
        def run(): Unit = {
          Thread.sleep(100 + scala.util.Random.nextInt(150)) // 用 sleep 模拟高延迟处理
          val result = BigDecimal(Math.sqrt(input)).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
          originalSender ! result
        }
      })
  }
}

class SqrtActorHigh extends Actor {
  override def receive: Receive = {
    case input: Int =>
      val originalSender = sender()
      // 用 Akka 的调度器（scheduler）来模拟高延迟处理
      context.system.scheduler.scheduleOnce((100 + scala.util.Random.nextInt(150)).millis) {
        val result = BigDecimal(Math.sqrt(input)).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
        originalSender ! result
      }
  }
}

object SqrtActorLow {
  def props: Props = Props[SqrtActorLow]
}

object SqrtActorHigh {
  def props: Props = Props[SqrtActorHigh]
}
