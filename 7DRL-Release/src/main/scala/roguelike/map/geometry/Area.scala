package roguelike.map.geometry

import scala.collection.immutable.Queue

case class Area(points: Stream[Point]) {
  def walkDiagonals = true
  
  lazy val pointSet = Set(points: _*)
  
  def floodFill(from: Point, walkDiagonals: Boolean): Area = {
    var found: Set[Point] = Set(from)
    var queue: Queue[Point] = new Queue(from)
    def doFill: Stream[Point] = {
      if (queue.isEmpty)
        Stream.empty
      else {
        val point = queue.front
        val newPoints = (point.neighbours(walkDiagonals).filter(p => pointSet.contains(p) && !found.contains(p)))
        found ++= newPoints
        queue = queue.dequeue._2 enqueue newPoints
        Stream.cons(point, doFill)
      }
    }
    Area(doFill)
  }
  def floodFill(from: Point): Area = floodFill(from, walkDiagonals)
  
  def connectedAreas(walkDiagonals: Boolean): Stream[Area] = {
    if (points.isEmpty)
      Stream.empty
    else {
      val area1 = floodFill(points.head, walkDiagonals)
      Stream.cons(area1, (this -- area1).connectedAreas(walkDiagonals))
    }
  }
  def connectedAreas: Stream[Area] = connectedAreas(walkDiagonals)
  
  def split(splitWith: Area, walkDiagonals: Boolean): Stream[Area] = (this -- splitWith).connectedAreas(walkDiagonals)
  def split(splitWith: Area): Stream[Area] = split(splitWith, walkDiagonals)
  
  def touches(p: Point, walkDiagonals: Boolean): Boolean = {
    points.exists(_.neighbours(walkDiagonals).contains(p))
  }
  def touches(p: Point): Boolean = touches(p, walkDiagonals)
  
  def border(walkDiagonals: Boolean): Area = {
    Area(points filter (!_.neighbours(walkDiagonals).forall(pointSet.contains)))
  }
  def border: Area = border(walkDiagonals)
  
  def center: Point = (points reduceLeft (_ + _)) / points.size
  
  lazy val boundingBox: (Int, Int, Int, Int) = {
    var (minX, minY, maxX, maxY) = (Int.MaxValue, Int.MaxValue, Int.MinValue, Int.MinValue)
    for (p <- points) {
      if (p.x < minX) minX = p.x
      if (p.y < minY) minY = p.y
      if (p.x > maxX) maxX = p.x
      if (p.y > maxY) maxY = p.y
    }
    (minX, minY, maxX, maxY)
  }
  
  def boundingBoxContains(p: Point) = {
    boundingBox._1 <= p.x && boundingBox._2 <= p.y && boundingBox._3 >= p.x && boundingBox._4 >= p.y
  }
  
  def translate(to: Point) = Area(points map to.+)
  
  def filter(f: Point => Boolean): Area = Area(points filter f)
  
  def --(other: Area) = {
    filter(!other.pointSet.contains(_))
  }
  def **(other: Area) = {
    filter(other.pointSet.contains(_))
  }
  def ++(other: Area) = Area((this.pointSet ++ other.points).toStream)
}
