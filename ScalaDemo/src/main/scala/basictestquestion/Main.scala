package basictestquestion

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

  private val parser = new ConfigParser()
  private val result: ConfigParser.Result = parser.parse(configStr)
//  println(result)
  println(ConfigParser.toPrettyString(result))
}
