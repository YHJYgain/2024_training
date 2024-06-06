package example

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main {
  def main(args: Array[String]): Unit = {
    val f = Future {
      2 / 0
    }
    for (exc <- f.failed) println(exc)

    f.failed.foreach(exc => println(exc))
  }
}
