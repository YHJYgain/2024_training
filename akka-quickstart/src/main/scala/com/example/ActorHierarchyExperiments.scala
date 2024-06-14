package com.example

import akka.actor.typed.{ActorSystem, Behavior, PostStop, PreRestart, Signal, SupervisorStrategy}
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors

// 一个 Actor，用于创建并打印另一个 Actor 的引用
object PrintMyActorRefActor {
  // 创建这个 Actor 的行为
  def apply(): Behavior[String] =
    Behaviors.setup(context => new PrintMyActorRefActor(context))
}

private class PrintMyActorRefActor(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "printit" =>
        val secondRef = context.spawn(Behaviors.empty[String], "second-actor") // 创建一个新的 Actor（second-actor）
        println(s"Second: $secondRef")
        this
    }
}

object StartStopActor1 {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new StartStopActor1(context))
}

private class StartStopActor1(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  println("first started")
  context.spawn(StartStopActor2(), "second")

  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "stop" => Behaviors.stopped
    }

  // 处理 PostStop 信号，当 Actor 停止时打印 "first stopped"
  override def onSignal: PartialFunction[Signal, Behavior[String]] = {
    case PostStop =>
      println("first stopped")
      this
  }
}

object StartStopActor2 {
  def apply(): Behavior[String] =
    Behaviors.setup(new StartStopActor2(_))
}

private class StartStopActor2(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  println("second started")

  override def onMessage(msg: String): Behavior[String] = {
    // 此 actor 不处理任何消息
    Behaviors.unhandled
  }

  override def onSignal: PartialFunction[Signal, Behavior[String]] = {
    case PostStop =>
      println("second stopped")
      this
  }
}

// 一个监督 Actor，它管理一个子 Actor
object SupervisingActor {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new SupervisingActor(context))
}

private class SupervisingActor(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  private val child = context.spawn(
    Behaviors.supervise(SupervisedActor()).onFailure(SupervisorStrategy.restart), // 子 Actor 失败时将被重新启动
    name = "supervised-actor")

  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "failChild" =>
        child ! "fail"
        this
    }
}

// 一个受监督的 Actor
object SupervisedActor {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new SupervisedActor(context))
}

private class SupervisedActor(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  println("supervised actor started")

  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "fail" =>
        println("supervised actor fails now")
        throw new Exception("I failed!")
    }

  override def onSignal: PartialFunction[Signal, Behavior[String]] = {
    case PreRestart =>
      println("supervised actor will be restarted")
      this
    case PostStop =>
      println("supervised actor stopped")
      this
  }
}

// 一个 Actor，用于启动 Actor 系统
object Main {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new Main(context))
}

private class Main(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "start" =>
//        val firstRef = context.spawn(PrintMyActorRefActor(), "first-actor")
//        println(s"First: $firstRef")
//        firstRef ! "printit"

//        val first = context.spawn(StartStopActor1(), "first")
//        first ! "stop"

        val supervisingActor = context.spawn(SupervisingActor(), "supervising-actor")
        supervisingActor ! "failChild"

        this
    }
}

object ActorHierarchyExperiments extends App {
  private val testSystem = ActorSystem(Main(), "testSystem") // 创建了一个 ActorSystem，并使用 Main 作为根行为
  testSystem ! "start" // 系统启动后，向 ActorSystem 发送 "start" 消息，触发 Main Actor 的逻辑
}
