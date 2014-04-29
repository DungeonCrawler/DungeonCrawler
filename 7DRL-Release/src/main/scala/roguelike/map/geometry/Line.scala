package roguelike.map.geometry

trait Lines {
  def apply(start: Point, end: Point): Area = {
    Area(ray(start, end.x - start.x, end.y - start.y).takeWhile(_ != end) append Stream(end))
  }
  
  def apply(start: Point, length: Int, angle: Int): Area = {
    val rad = Math.toRadians(angle)
    apply(start, start + Point((Math.cos(rad) * length).toInt, (Math.sin(rad) * length).toInt))
  }
  
  def lineIn(area: Area, start: Point, angle: Int): Area = {
    val rad = Math.toRadians(angle)
    val multiplier = 100
    lineIn(area, start, (Math.cos(rad) * multiplier).toInt, (Math.sin(rad) * multiplier).toInt)
  }
  
  def lineIn(area: Area, start: Point, dx: Int, dy: Int): Area = {
    rayIn(area, start, dx, dy) ++ rayIn(area, start, -dx, -dy)
  }
  
  def rayIn(area: Area, start: Point, angle: Int): Area = {
    val rad = Math.toRadians(angle)
    val multiplier = 100
    rayIn(area, start, (Math.cos(rad) * multiplier).toInt, (Math.sin(rad) * multiplier).toInt)
  }
  
  def rayIn(area: Area, start: Point, dx: Int, dy: Int): Area = {
    Area(ray(start, dx, dy).takeWhile(area.boundingBoxContains).filter(area.pointSet.contains))
  }
  
  private def boxesTouch(r1: (Int, Int, Int, Int), r2: (Int, Int, Int, Int)) = {
    val (r1x1, r1y1, r1x2, r1y2) = r1
    val (r2x1, r2y1, r2x2, r2y2) = r2
    def segmentsTouch(a1: Int, a2: Int, b1: Int, b2: Int): Boolean = {
      if (a1 > a2) segmentsTouch(a2, a1, b1, b2)
      else if (b1 > b2) segmentsTouch(a1, a2, b2, b1)
      else if (a1 < b1) a2 > b1 else if (a1 < b2) a2 > b2 else false
    }
    segmentsTouch(r1x1, r1x2, r2x1, r2x2) && segmentsTouch(r1y1, r1y2, r2y1, r2y2)
  }
  
  def ray(start: Point, angle: Int): Stream[Point] = {
    val rad = Math.toRadians(angle)
    val multiplier = 100
    ray(start, (Math.cos(rad) * multiplier).toInt, (Math.sin(rad) * multiplier).toInt)
  }

  def ray(start: Point, dx: Int, dy: Int): Stream[Point]
}

object Line extends Lines {
  def ray(start: Point, dx: Int, dy: Int): Stream[Point] = {
    val dy2 = Math.abs(dy) * 2
    val dx2 = Math.abs(dx) * 2
    val stepy = Math.signum(dy)
    val stepx = Math.signum(dx)
    
    Stream.cons(start,
      if (dx2 > dy2) {
        var fraction = dy2 - dx2 / 2
        var y = start.y
        def step(x: Int): Stream[Point] = {
          if (fraction >= 0) {
            y += stepy
            fraction -= dx2
          }
          fraction += dy2
          Stream.cons(Point(x, y), step(x + stepx))
        }
        step(start.x + stepx)
      } else {
        var fraction = dx2 - dy2 / 2
        var x = start.x
        def step(y: Int): Stream[Point] = {
          if (fraction >= 0) {
            x += stepx
            fraction -= dy2
          }
          fraction += dx2
          Stream.cons(Point(x, y), step(y + stepy))
        }
        step(start.y + stepy)
    })
  }
}

object ThickLine extends Lines {
  def ray(start: Point, dx: Int, dy: Int): Stream[Point] = {
    val dy2 = Math.abs(dy) * 2
    val dx2 = Math.abs(dx) * 2
    val stepy = Math.signum(dy)
    val stepx = Math.signum(dx)
    
    Stream.cons(start,
      if (dx2 > dy2) {
        var fraction = dy2 - dx2 / 2
        var y = start.y
        def step(x: Int): Stream[Point] = {
          if (fraction >= 0) {
            y += stepy
            fraction -= dx2
            fraction += dy2
            Stream.cons(Point(x - stepx, y), Stream.cons(Point(x, y), step(x + stepx)))
          } else {
            fraction += dy2
          	Stream.cons(Point(x, y), step(x + stepx))
          }
        }
        step(start.x + stepx)
      } else {
        var fraction = dx2 - dy2 / 2
        var x = start.x
        def step(y: Int): Stream[Point] = {
          if (fraction >= 0) {
            x += stepx
            fraction -= dy2
            fraction += dx2
            Stream.cons(Point(x, y - stepy), Stream.cons(Point(x, y), step(y + stepy)))
          } else {
            fraction += dx2
          	Stream.cons(Point(x, y), step(y + stepy))
          }
        }
        step(start.y + stepy)
    })
  }
}
