package roguelike

import scala.collection.mutable.PriorityQueue

object TimeLine {
  private val queue = new PriorityQueue[Event]
  
  private class Event(val time: Long, val func: () => Unit) extends Ordered[Event] {
    def compare(that: Event) = (that.time - this.time).toInt

    override def toString = "@" + time
  }
  
  private var now: Long = 0

  def time = now
  
  def schedule(timeFromNow: Long)(func: => Unit) {
    queue += new Event(now + timeFromNow, () => func)
  }
  
  def isEmpty = queue.isEmpty
  def step() = {
    val event = queue.dequeue
    now = event.time
    event.func()
  }

  def clear = {
    queue.clear
  }
}
