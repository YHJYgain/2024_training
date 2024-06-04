package basictestquestion

import scala.util.matching.Regex

object ConfigParser {

  // 定义配置项的数据结构
  case class SwitchConfig(
                           name: String, // 开关的名称
                           depList: List[Int], // 依赖的模块 id 列表. 如果为空，需要报错
                           metaInfo: Map[String, String] = Map(), // 一些元数据。默认为空 Map
                           enabled: Boolean = true // 是否激活. 默认为 true
                         )

  // 定义解析结果的数据结构，包含解析后的数据或错误信息
  case class Result(
                     data: Option[Map[String, SwitchConfig]],  // 解析后的数据
                     error: Option[String] // 第一个错误的信息
                   )

  def toPrettyString(result: Result): String = {
    result match {
      case Result(Some(data), None) =>
        val switches = data.map { case (name, config) =>
          s"Switch: $name\n" +
            s"  Enabled: ${config.enabled}\n" +
            s"  DepList: ${config.depList.mkString("[", ", ", "]")}\n" +
            s"  MetaInfo: ${config.metaInfo.map { case (k, v) => s"$k: $v" }.mkString("{", ", ", "}")}\n"
        }.mkString("\n")
        s"Configuration:\n$switches"
      case Result(None, Some(error)) =>
        s"Error: $error"
      case _ =>
        "Invalid result"
    }
  }

}

class ConfigParser {

  import ConfigParser._

  def parse(configStr: String): Result = {
    val lines = configStr.split("\r?\n").filter(_.trim.nonEmpty) // 将配置字符串按行分割并过滤掉空行
    val configMap = scala.collection.mutable.Map[String, SwitchConfig]() // 用于存储解析后的配置数据

    // 匹配配置项的名称和属性
    val namePattern: Regex = "([a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9])\\.([a-zA-Z0-9_.]+)\\s*=\\s*(.+)".r
    // 匹配依赖列表的格式
    val listPattern: Regex = "\\[(\\s*\\d+(\\s*,\\s*\\d+)*)]".r
    // 匹配布尔值
    val booleanPattern: Regex = "(true|false)".r

    var error: Option[String] = None

    for (line <- lines if error.isEmpty) {
      line match {
        case namePattern(switch, property, value) =>
          if (!configMap.contains(switch)) {
            configMap(switch) = SwitchConfig(name = switch, depList = List())
          }

          property match {
            case "enabled" =>
              value.trim match {
                case booleanPattern(b) =>
                  configMap(switch) = configMap(switch).copy(enabled = b.toBoolean)
                case _ =>
                  error = Some(s"$switch.enabled: Invalid boolean value")
              }

            case "depList" =>
              value.trim match {
                case listPattern(list, _) =>
                  val depList = list.split(",").map(_.trim.toInt).toList
                  if (depList.nonEmpty) {
                    configMap(switch) = configMap(switch).copy(depList = depList)
                  } else {
                    error = Some(s"$switch.depList: List cannot be empty")
                  }
                case _ =>
                  error = Some(s"$switch.depList: Invalid list format")
              }

            case metaInfo if metaInfo.startsWith("metaInfo.") =>
              val key = metaInfo.stripPrefix("metaInfo.")
              val metaInfoMap = configMap(switch).metaInfo + (key -> value.trim)
              configMap(switch) = configMap(switch).copy(metaInfo = metaInfoMap)

            case _ =>
              error = Some(s"$switch.$property: Unknown property")
          }

        case _ =>
          error = Some(s"Invalid format: $line")
      }
    }

    error match {
      case Some(err) => Result(None, Some(err))
      case None => Result(Some(configMap.toMap), None)
    }
  }

}
