# 基于 Play Framework + AKKA Actor 的服务器高延迟 API

## 介绍

- 基于 Play Framework + AKKA Actor 实现服务器高延迟 API

  - **输入**：一个整形数据

  - **输出**：返回输入数据的平方根（浮点型，保留 2 位小数）

  - **延迟**：模拟业务处理耗时 100ms ~ 250ms，延迟大于 200ms 则视为超时，算请求失败，记录异常日志。

- 已实现以下功能：
  1. 基于 Play Action 实现限流能力，限流值（5）配置在本地配置文件 [./conf/application.conf](https://github.com/YHJYgain/2024_training/blob/master/ExamineDemo/conf/application.conf) 中。
  2. 日志分级：Error/Warn 日志与 Info 日志区分开。
  3. AKKA Actor 实现低高级业务逻辑：
     - Actor 有自定义超时逻辑（5s），防止 Actor 超时太长，不能及时退出。
     - 【低级】用 sleep 模拟高延迟处理：用线程数量固定且队列有界的自定义线程池隔离 sleep 阻塞逻辑。
     - 【高级】用 Future/Promise 异步逻辑模拟高延迟，定时一段时间后，再返回结果。
- 服务性能要求：
  - 堆内存最多 4GB，使用 G1 作为垃圾回收器。
  - Play 框架和 Actor 框架线程池都不超过 CPU 核数。
  - CPU 总消耗不能超过物理机的 50%。

## 开发环境

| 名称         | 概述                                                         |
| ------------ | ------------------------------------------------------------ |
| 操作系统     | [Windows 11](https://www.microsoft.com/zh-cn/software-download/windows11) |
| 集成开发环境 | [IntelliJ IDEA 2022.2.4](https://www.jetbrains.com/idea/)    |
| 编程语言     | [Scala 2.11.12](https://www.scala-lang.org/)                 |
| JDK          | [Amazon Corretto 8](https://docs.aws.amazon.com/zh_cn/corretto/latest/corretto-8-ug/downloads-list.html) |
| 构建工具     | [sbt 0.13.18](https://www.scala-sbt.org/download)、[Docker Desktop 4.30.0](https://www.docker.com/products/docker-desktop/) |
| 框架         | [Play Framework 2.5.18](https://www.playframework.com/documentation/2.5.x/Installing)、[Akka 2.5.13](https://akka.io/) |

## 如何开始

1. 下载 [Docker Desktop](https://www.docker.com/products/docker-desktop/)，注册并打开。

2. clone 项目。

3. 运行项目根目录下的 [./build.sh](https://github.com/YHJYgain/2024_training/blob/master/ExamineDemo/build.sh) 文件。（此时默认已下载好 bash）

4. 运行完成之后，打开 Docker Desktop，可以看到 Docker 容器已经在运行：

   ![](https://gitee.com/ReGinWZY/figure-bed/raw/master/TyporaImg/202406181822951.png)

5. 打开浏览器，访问：（最后的数字可任意更改）

   - 【低级】：http://localhost:9000/lowerSqrt/4
   - 【高级】：http://localhost:9000/higherSqrt/4

