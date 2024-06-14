import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import actors.{SqrtActorLow, SqrtActorHigh}

// 一个 Guice 模块，用于配置依赖注入
class Module extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bindActor[SqrtActorLow]("sqrtActorLow")
    bindActor[SqrtActorHigh]("sqrtActorHigh")
  }
}
