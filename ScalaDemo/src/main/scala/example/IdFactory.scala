package example

object IdFactory {
  private var counter = 0
  def create(): Int = {
    counter += 1
    counter
  }
}
