package roguelike.map.geometry

case class Point(x: Int, y: Int) {
  def +(other: Point) = Point(x + other.x, y + other.y)
  def -(other: Point) = Point(x - other.x, y - other.y)
  def *(s: Int) = Point(x * s, y * s)
  def /(s: Int) = Point(x / s, y / s)
  
  def lengthSquare = x*x + y*y
  def distSquare(other: Point) = (this - other).lengthSquare
  def length = Math.sqrt(lengthSquare).toInt
  def dist(other: Point) = (this - other).length

  def rogueLength = Math.max(x, y)
  def rogueDist(other: Point) = (this - other).rogueLength
  
  def neighbours(walkDiagonals: Boolean): Set[Point] = if (walkDiagonals) (neighbours ++ diagonalNeighbours) else neighbours
  def neighbours = Set(Point(x - 1, y), Point(x, y - 1), Point(x + 1, y), Point(x, y + 1))
  def diagonalNeighbours = Set(Point(x - 1, y - 1), Point(x + 1, y - 1), Point(x + 1, y + 1), Point(x - 1, y + 1))
  def neighbourhood(walkDiagonals: Boolean): Set[Point] = neighbours(walkDiagonals) + this
  
  override def toString = "(" + x + "," + y + ")"
}