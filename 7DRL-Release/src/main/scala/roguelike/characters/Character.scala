package roguelike.characters

import roguelike.map.{TileMap, TranslatedMapView}
import roguelike.map.geometry.{Point, Line}
import roguelike.map.fov._
import roguelike.map.path.MapPathFinder
import roguelike.combat.Combatant
import roguelike.util.Random

trait Character extends Combatant {
  var location = Point(0, 0)
  
  private var _map: TileMap = null
  
  def controller: Controller

  var lastMove: Long = 0
  def waitingTime = TimeLine.time - lastMove

  def description: String
  
  def map = _map
  def currentTile = map.getTile(location)
  
  def changeMap(m: TileMap): Unit = {
    if (_map != null) _map.characters -= this
    if (m != null) m.characters ::= this
    _map = m
  }
  
  def leaveMap = changeMap(null)
  
  override def comeToLife = {
    super.comeToLife

    assert(map != null)
    
    def act: Unit = {
      if (alife) {
        var action = controller.act
        while (!action.valid(this)) action = controller.act
        action.execute(this)
        TimeLine.schedule(action.duration)(act)
      }
    }
    
    act
  }

  private var mapFoVVersion = 0
  private lazy val visibilityMapView: TranslatedMapView = new TranslatedMapView(map.visibilityView)
  private lazy val fov: FieldOfView = new WallLighter(new DiamondFieldOfView(visibilityMapView, 20))

  def inFoV(p: Point) = {
    assert(map != null)
    assert(visibilityMapView != null)
    assert(fov != null)
    if (mapFoVVersion != map.version || visibilityMapView.getX != location.x || visibilityMapView.getY != location.y || visibilityMapView.getMap != map.visibilityView) {
      mapFoVVersion = map.version
      visibilityMapView.setMap(map.visibilityView)
      visibilityMapView.setX(location.x)
      visibilityMapView.setY(location.y)
      fov.update
    }
    fov.isVisible(p.x - location.x, p.y - location.y)
  }

  def visibility(p: Point): Visibility = {
    if (!inFoV(p))
      NotVisible
    else if (map.getTile(p).blocking) {
      var neighbours = p.neighbours(false).filter(p => !map.getTile(p).blocking && inFoV(p))
      if (neighbours.isEmpty)
        neighbours = p.diagonalNeighbours.filter(!map.getTile(_).blocking)
      val visibilities = neighbours map visibility
      if (visibilities contains FullyVisible)
        FullyVisible
      else if ((visibilities contains Visible) || (location.neighbourhood(true) contains p))
        Visible
      else
        NotVisible
    }
    else {
      if (map.lit(p)) {
        if (map.fullyLit(p))
          FullyVisible
        else
          Visible
      } else if (location.neighbourhood(true) contains p)
        Visible
      else
        NotVisible
    }
  }

  def canHear(from: Point, loudness: Int): Boolean = {
    if ((from rogueDist location) > loudness)
      false
    else {
      //val pathFinder = new MapPathFinder(map.soundPassabilityView, true)
      //pathFinder.maxPathLength = Some(loudness)
      //pathFinder.findPath(from, location) != None
      def soundResistance(loc: Point) = if (map.getTile(loc).blocking) 5 else 1
      def traceSound(points: Stream[Point], lostLoudness: Int): Boolean = {
        if (lostLoudness > loudness)
          false
        else if (points.isEmpty)
          true
        else {
          traceSound(points.tail, lostLoudness + soundResistance(points.head))
        }
      }
      traceSound(Line(location, from).points, 0)
    }
  }

  def detects(from: Point, detectionDifficulty: Int): Boolean = {
    val vis = visibility(from)
    if (vis == NotVisible)
      false
    else {
      val currentDetect = Math.max(1, detect + (if (attentive) 0 else -4) + (if (vis == FullyVisible) 2 else -4))
      val d = Random.nd0m(currentDetect, 8)
      //util.Debug.log("Detect throw by " + this.description + ": " + d + "/" + currentDetect + " against " + detectionDifficulty)
      d > detectionDifficulty
    }
  }

  def emitMessage(msg: Message) = {
    map.emitMessage(location, msg)
  }

  def acceptMessage(msg: Message) = {
    controller.handleMessage(msg)
  }

  def lightSources: List[roguelike.map.LightSource] = Nil

  def detect: Int
  def hidingValue: Int = agility / 2 + hidingModifier
  def sneakingValue: Int = agility / 2 + sneakingModifier
  def attentive = true

  def hidingModifier = {
    ((if (location.neighbours(false).exists(map.getTile(_).blocking)) 2 else -2) + // Walls
     (if (lightSources.isEmpty) 0 else -12)) // Carrying a torch makes you not very stealthy
  }

  def sneakingModifier = {
    0
  }
}

trait Controller {
  def act(): Action
  def handleMessage(msg: Message): Unit = { }
}

object DummyController extends Controller {
  def act = {
    Wait
  }
}

abstract sealed class Visibility {
  def visible: Boolean
}
final case object FullyVisible extends Visibility {
  def visible = true
}
final case object Visible extends Visibility {
  def visible = true
}
final case object NotVisible extends Visibility {
  def visible = false
}

class CharacterMessage(character: Character) extends Message {
  override def detectionDifficulty = character.hidingValue
  override def involved = character :: super.involved
}

