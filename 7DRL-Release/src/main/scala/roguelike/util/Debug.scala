package roguelike.util

object Debug {
  def log(str: String) = if (enabled) println(str)

  def enabled = false
}
