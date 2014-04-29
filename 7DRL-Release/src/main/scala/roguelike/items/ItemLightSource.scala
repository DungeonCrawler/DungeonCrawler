package roguelike.items

import roguelike.map.LightSource
import roguelike.map.TileMap
import roguelike.map.geometry.Point
import roguelike.characters.Character

trait ItemLightSource extends Item {
  self =>
  def radius: Int

  def getLightSource(ch: Character): LightSource = getLightSource(ch.map, ch.location)

  def getLightSource(m: TileMap, loc: Point): LightSource = new LightSource {
    def radius = self.radius
    def location = loc
    def map = m
  }
}

class Torch extends SideArm with ItemLightSource {
  def description = "torch"
  def longDescription = "A torch. It lights you the way, but makes hiding rather difficult..."

  def glyph = '/'

  def radius = 6
}
