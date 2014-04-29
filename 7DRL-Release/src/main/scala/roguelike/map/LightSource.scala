package roguelike.map

import geometry.Point
import fov._

trait LightSource {
  def radius: Int
  def fullyLitRadius: Int = radius / 2

  def location: Point
  def map: TileMap

  private var mapFoVVersion = 0
  private lazy val visibilityMapView = new TranslatedMapView(map.visibilityView)
  private lazy val fov: FieldOfView = new DiamondFieldOfView(visibilityMapView, radius * 2)

  private def canSee(p: Point) = {
    if (mapFoVVersion != map.version || visibilityMapView.getX != location.x || visibilityMapView.getY != location.y || visibilityMapView.getMap != map.visibilityView) {
      mapFoVVersion = map.version
      visibilityMapView.setMap(map.visibilityView)
      visibilityMapView.setX(location.x)
      visibilityMapView.setY(location.y)
      fov.update
    }
    fov.isVisible(p.x - location.x, p.y - location.y)
  }

  def lit(p: Point) = p.distSquare(location) <= (radius * radius) && canSee(p)
  def fullyLit(p: Point) = p.distSquare(location) <= (fullyLitRadius * fullyLitRadius) && canSee(p)
}
