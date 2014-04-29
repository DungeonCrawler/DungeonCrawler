package roguelike.map.geometry

object Rect {
  def apply(corner: Point, w: Int, h: Int): Area = {
    if (w < 0) apply(corner + Point(w, 0), -w, h)
    else if (h < 0) apply(corner + Point(0, h), w, -h)
    else Area(for (x <- Stream.range(corner.x, corner.x + w); y <- Stream.range(corner.y, corner.y + h)) yield Point(x, y))
  }
  def apply(corner1: Point, corner2: Point): Area = apply(corner1, corner2.x - corner1.x, corner2.y - corner1.y)
}
