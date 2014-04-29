package roguelike.util

class Random(seed: Long) {
  def this() = this(System.currentTimeMillis)
  
  private val r = new scala.util.Random()
  
  def next = r.nextInt
  def next(max: Int) = r.nextInt(max)
  
  def next(min: Int, max: Int) = r.nextInt(max - min) + min
  
  def coin = next(2) == 1
  
  def binomial(width: Int, height: Int) = (0 until height).map(i => next(-(width / height), (width / height) + 1)).foldLeft(0)(_ + _)

  def chooseOne[T](from: Seq[T]): T = from(next(from.size))
  
  def choose[T](from: Seq[T], n: Int): Seq[T] = {
    if (n > from.size) error("choose with too few possibilities")
    var alreadyChosen: Set[Int] = Set.empty
    while (alreadyChosen.size < n) {
      def next: Int = {
        val n = Random.next(from.size)
        if (alreadyChosen contains n) next else n
      }
      alreadyChosen += next
    }
    for (n <- alreadyChosen.toSeq) yield from(n)
  }

  def maybe[T](chance: Int, value: => T): Maybe[T] = if (next(100) < chance) new MaybeWithValue(value) else MaybeWithoutValue

  abstract class Maybe[+T] {
    def or[U >: T](els: => U): U
    def or[U >: T](chance: Int, els: => U): Maybe[U]
  }

  private class MaybeWithValue[+T](value: T) extends Maybe[T] {
    def or[U >: T](els: => U) = value
    def or[U >: T](chance: Int, els: => U) = this
  }

  private object MaybeWithoutValue extends Maybe[Nothing] {
    def or[U >: Nothing](els: => U): U = els
    def or[U >: Nothing](chance: Int, els: => U): Maybe[U] = if (next(100) < chance) new MaybeWithValue(els) else this
  }

  def d02 = d0n(2)
  def nd02(n: Int) = nd0m(n, 2)

  def d0n(n: Int) = next(0, n + 1)
  def nd0m(n: Int, m: Int) = {
    ((1 to n*2/m) map (_ => d0n(m)) foldLeft (0)) (_ + _)
  }
}

object Random extends Random