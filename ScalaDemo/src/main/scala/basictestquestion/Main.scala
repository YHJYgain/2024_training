package basictestquestion

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Main extends App {

  val configStr: String =
    """
switchA.enabled = true
switchA.depList = [1, 2, 3]
switchA.metaInfo.owner = "userA"
switchA.metaInfo.comment = "hello world"

switchB.enabled = false
switchB.depList = [3, 4, 5]
switchB.metaInfo.owner = "userB"
switchB.metaInfo.comment = "hello world"
"""

  println("【bonus1：逆向操作、bonus2：状态维护】")
  private val parser = new ConfigParser()
  parser.parseLine("switchA.enabled = true")
  parser.parseLine("switchA.depList = [1, 2, 3]")
  parser.parseLine("switchA.metaInfo.owner = \"userA\"")
  parser.parseLine("switchA.metaInfo.comment = \"hello world\"")
  parser.parseLine("switchB.enabled = false")
  parser.parseLine("switchB.depList = [3, 4, 5]")
  parser.parseLine("switchB.metaInfo.owner = \"userB\"")
  parser.parseLine("switchB.metaInfo.comment = \"hello world\"")
  val result: ConfigParser.Result = parser.getResult
//  result.data.foreach(data => println(ConfigParser.stringify(data)))
  result match {
    case Right(data) => println(ConfigParser.stringify(data))
    case Left(error) => println(s"Error: $error")
  }

  println("-----------------------------------------------------------")

  println("【bonus3：多线程】")
  val configStrList: List[String] = List(configStr)
  private val resultFuture: Future[List[ConfigParser.Result]] = parser.parseAll(configStrList)
  private val resultList: List[ConfigParser.Result] = Await.result(resultFuture, 10.seconds)
  resultList.foreach(res => println(ConfigParser.toPrettyString(res)))

}