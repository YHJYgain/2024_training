package basictestquestion

import scala.util.matching.Regex
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ConfigParser {

  // 定义配置项的数据结构
  case class SwitchConfig(
                           name: String,
                           properties: Map[String, Any] = Map()
                         )

//  // 定义解析结果的数据结构，包含解析后的数据或错误信息
//  case class Result(
//                     data: Option[Map[String, SwitchConfig]],
//                     error: Option[String]
//                   )

  // 定义解析结果的数据结构，包含解析后的数据或错误信息
  type Result = Either[String, Map[String, SwitchConfig]]

  def stringify(configMap: Map[String, SwitchConfig]): String = {
    configMap.map { case (name, config) =>
      config.properties.map {
        case (key, value: List[_]) => s"$name.$key = [${value.mkString(", ")}]"
        case (key, value) => s"$name.$key = $value"
      }.mkString("\n")
    }.mkString("\n")
  }

//  def toPrettyString(result: Result): String = {
//    result match {
//      case Result(Some(data), None) =>
//        val switches = data.map { case (name, config) =>
//          s"Switch: $name\n" +
//            config.properties.map { case (k, v) =>
//              s"  $k: $v"
//            }.mkString("\n")
//        }.mkString("\n")
//        s"Configuration:\n$switches"
//      case Result(None, Some(error)) =>
//        s"Error: $error"
//      case _ =>
//        "Invalid result"
//    }
//  }

  def toPrettyString(result: Result): String = {
    result match {
      case Right(data) =>
        val switches = data.map { case (name, config) =>
          s"Switch: $name\n" +
            config.properties.map { case (k, v) =>
              s"  $k: $v"
            }.mkString("\n")
        }.mkString("\n")
        s"Configuration:\n$switches"
      case Left(error) =>
        s"Error: $error"
    }
  }

}

class ConfigParser {

  import ConfigParser._

  // 匹配配置项的名称和属性
  private val namePattern: Regex = "([a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9])\\.([a-zA-Z0-9_.]+)\\s*=\\s*(.+)".r
  // 匹配依赖列表的格式
  private val listPattern: Regex = "\\[(\\s*\\d+(\\s*,\\s*\\d+)*)]".r
  // 匹配布尔值
  private val booleanPattern: Regex = "(true|false)".r

  private val configMap = scala.collection.mutable.Map[String, SwitchConfig]()
  private var error: Option[String] = None

  private def parse(configStr: String): Result = {
    val lines = configStr.split("\r?\n").filter(_.trim.nonEmpty) // 将配置字符串按行分割并过滤掉空行
    for (line <- lines if error.isEmpty) {
      parseLine(line)
    }

    getResult
  }

  def parseLine(line: String): Unit = {
    line match {
      case namePattern(switch, property, value) =>
        if (!configMap.contains(switch)) {
          configMap(switch) = SwitchConfig(name = switch)
        }

        val currentConfig = configMap(switch)
        val parsedValue = parseValue(value.trim)
        val updatedProperties = currentConfig.properties + (property -> parsedValue)
        configMap(switch) = currentConfig.copy(properties = updatedProperties)

      case _ =>
        error = Some(s"Invalid format: $line")
    }
  }

  private def parseValue(value: String): Any = {
    value match {
      case booleanPattern(b) => b.toBoolean
      case listPattern(list, _) => list.split(",").map(_.trim).filter(_.nonEmpty).map(_.toInt).toList
      case _ if value.forall(_.isDigit) => value.toInt
      case _ => value
    }
  }

//  def getResult(): Result = {
//    error match {
//      case Some(err) => Result(None, Some(err))
//      case None => Result(Some(configMap.toMap), None)
//    }
//  }

  def getResult: Result = {
    error match {
      case Some(err) => Left(err)
      case None => Right(configMap.toMap)
    }
  }

  def parseAll(configStrList: List[String]): Future[List[Result]] = {
    val futures = configStrList.map(configStr => Future {
      val parser = new ConfigParser()
      parser.parse(configStr)
    })

    Future.sequence(futures)
  }

}