package roguelike.map

import scala.collection.mutable.{Map => MutableMap}
import roguelike.map.geometry.{Point, Area}
import roguelike.characters.Character
import roguelike.items.Item

class TileMap {
  private val map = MutableMap[Point, Tile]()
  
  private val defaultTile: Tile = Wall

  private var _version = 0
  def version = _version
  def bumpVersion = _version += 1
  
  var characters: List[Character] = Nil
  def getCharacterAt(p: Point) = characters.find(_.location == p)
  var items: List[Item] = Nil
  
  var lightSources: List[LightSource] = Nil

  def allLightSources = lightSources ::: characters.flatMap(_.lightSources)

  def lit(p: Point) = allLightSources.exists(_.lit(p))
  def fullyLit(p: Point) = allLightSources.exists(_.fullyLit(p))
  
  val passabilityView: MapView = new MapView {
    def get(x: Int, y: Int) = if (getTile(Point(x, y)).blocking) -1 else 1
  }
  
  val visibilityView: MapView = passabilityView

  val soundPassabilityView: MapView = new MapView {
    def get(x: Int, y: Int) = if (getTile(Point(x, y)).blocking) 4 else 1
  }
  
  def getTile(p: Point) = map.get(p).getOrElse(defaultTile)
  
  def setTile(p: Point, tile: Tile) = {
    map += (p -> tile)
    bumpVersion
  }
  def clearTile(p: Point) = {
    map.removeKey(p)
    bumpVersion
  }
  
  def setArea(area: Area, tile: Tile) = {
    for (p <- area.points) {
      setTile(p, tile)
    }
  }
  def clearArea(area: Area) = area.points foreach clearTile

  def emitMessage(from: Point, msg: Message) = {
    val watchingCharacters = characters.filter(ch => msg.involved.contains(ch) || ch.detects(from, msg.detectionDifficulty))
    watchingCharacters.foreach(_.acceptMessage(msg))
    //util.Debug.log("Watching characters: " + watchingCharacters.size)
    for (noise <- msg.noise) {
      val listeningCharacters = characters.filter(ch => !watchingCharacters.contains(ch) && ch.canHear(from, noise.loudness))
      //util.Debug.log("Listening characters: " + listeningCharacters.size)
      listeningCharacters.foreach(_.acceptMessage(noise))
    }
  }
}

abstract class Tile {
  def blocking: Boolean
}

case object Wall extends Tile {
  def blocking = true
}

case object Floor extends Tile {
  def blocking = false
}

class TorchHolder extends Tile {
  def blocking = false

  def hasTorch = torch.isDefined

  private var torch: Option[items.Torch] = None
  private var lightSource: Option[LightSource] = None

  def put(t: items.Torch, map: TileMap, loc: Point) = {
    assert(torch.isEmpty)
    assert(lightSource.isEmpty)
    torch = Some(t)
    lightSource = Some(t.getLightSource(map, loc))
    map.lightSources ::= lightSource.get
  }

  def get(map: TileMap) = {
    assert(torch.isDefined)
    assert(lightSource.isDefined)
    map.lightSources -= lightSource.get
    val t = torch.get
    torch = None
    lightSource = None
    t
  }
}

case class Door(var open: Boolean) extends Tile {
  def blocking = !open
}

case class DownStairs(lowerMap: () => TileMap) extends Tile {
  def blocking = false
}

case class UpStairs(higherMap: () => TileMap) extends Tile {
  def blocking = false
}

case object DungeonExit extends Tile {
  def blocking = false
}
