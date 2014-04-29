package roguelike.map

import roguelike.map.geometry._
import roguelike.items._
import roguelike.characters._
import roguelike.util.Random

object Dungeon {
  var levels: Map[Int, TileMap] = Map.empty
  var populated: Set[TileMap] = Set.empty
  val levelWidth = 50//30
  val levelHeight = 20//30
  val dungeonDepth = 6

  def reset = {
    levels = Map.empty
    populated = Set.empty
  }
  
  def getLevel(lvl: Int): TileMap = {
    val map = levels.get(lvl).getOrElse(generateMap)
    levels += ((lvl, map))
    if (!populated.contains(map)) populateMap(lvl, map)
    map
  }

  def getDepthOfMap(map: TileMap): Option[Int] = {
    levels.find(_._2 == map).map(_._1)
  }
  
  def getUnpopulatedLevel(lvl: Int): TileMap = {
    val map = levels.get(lvl).getOrElse(generateMap)
    levels += ((lvl, map))
    map
  }
  
  private def populateMap(lvl: Int, map: TileMap) = {
    util.Debug.log("Populating level " + lvl)
    populated += map
    val lowerMap = getUnpopulatedLevel(lvl + 1)
    placeTorches(map)
    if (lvl < dungeonDepth)
      placeStairs(lvl, map, lowerMap)
    placeItems(lvl, map)
    placeMonsters(lvl, map)
  }
  
  def randomLocations(map: TileMap): Stream[Point] = {
    Stream.cons(Point(Random.next(levelWidth), Random.next(levelHeight)), randomLocations(map))
  }
  def freeLocations(map: TileMap) = randomLocations(map).filter(map.getTile(_) == Floor)
  def monsterFreeLocations(map: TileMap) = freeLocations(map).filter(!map.getCharacterAt(_).isDefined)

  private def placeTorches(map: TileMap) = {
    for (loc <- freeLocations(map).take(Random.next(5, 10))) {
      val t = new TorchHolder
      map.setTile(loc, t)
      t.put(new Torch, map, loc)
    }
  }
  
  private def placeItems(lvl: Int, map: TileMap) = {
    
  }
  
  private def placeMonsters(lvl: Int, map: TileMap) = {
    val monsterCount = Random.next(15, 25)
    for (i <- 1 to monsterCount) {
      val monster = generateMonster(lvl)
      monster.changeMap(map)
      val candidates = monsterFreeLocations(map) take 3
      val litCandidates = candidates filter map.lit
      util.Debug.log("No lit candidates: " + litCandidates.isEmpty)
      if (litCandidates.isEmpty)
        monster.location = candidates.first
      else
        monster.location = litCandidates.first
      monster.comeToLife
    }
    if (lvl == dungeonDepth) {
      placeBoss(map)
    }
  }
  
  def placeBoss(map: TileMap) {
    GoblinKing.changeMap(map)
    val candidate = monsterFreeLocations(map) take 100
    val lit = candidate filter map.lit
    val fullyLit = lit filter map.fullyLit
    GoblinKing.location = fullyLit.firstOption orElse lit.firstOption getOrElse candidate.first // Always try to put the king at a fully lit location
    GoblinKing.comeToLife
    // Place his guard
    def generateBossGuard = (Random.maybe(40, new GoblinElite with GoblinKingsGuard) or 
                             (50, new GoblinArcher with GoblinKingsGuard) or
                             (new GoblinGuard with GoblinKingsGuard))
    val guardCount = Random.next(5, 9)
    val guardLocations = monsterFreeLocations(map).take(1000).filter(_.distSquare(GoblinKing.location) < 4*4).take(guardCount)
    // take(1000) to guard against an infinite loop if there are no monster-free locations near the king
    // (very improbable, but possible!)
    val guards = guardLocations map { loc =>
      val guard = generateBossGuard
      guard.changeMap(map)
      guard.location = loc
      guard
    }
    guards.foreach (_.comeToLife)
  }
  
  private def placeStairs(lvl: Int, map: TileMap, lowerMap: TileMap): Unit = {
    val possibleStairs = freeLocations(map) filter (lowerMap.getTile(_) == Floor)
    if (possibleStairs.isEmpty) {
      error("Could not place stairs!")
    } else {
      val stairCount = Random.next(1, 4)
      val downStairs = DownStairs(() => getLevel(lvl + 1))
      val upStairs = UpStairs(() => map)
      for (p <- possibleStairs.take(stairCount)) {
        map.setTile(p, downStairs)
        lowerMap.setTile(p, upStairs)
      }
    }
  }
  
  def generateItem = {
    Random.maybe(50, new Dagger).or(new LeatherArmour)
  }
  
  def generateMonster(lvl: Int): NPC = {
    if (lvl > 3)
      Random.maybe(10, new GoblinElite).or(generateMonster(lvl - 1))
    else if (lvl > 1)
      Random.maybe(15, new GoblinArcher).or(generateMonster(lvl - 1))
    else
      Random.maybe(50, new GoblinWarrior).or(new GoblinGuard)
  }
  
  private def generateMap = {
    val map = new TileMap
    generateSubdivMap(map, Rect(Point(0, 0), levelWidth, levelHeight),
                      Line(Point(0, -1), Point(levelWidth - 1, 0)),
                      Math.max(10, Random.next(-25, 120)),
                      0, Random.nd0m(20, 8) - 20,
                      doorChooser, 30,
                      (wall: Area) => wall.center.neighbourhood(true).filter(wall.points.contains).toStream.head)
    map
  }

  private def doorChooser(candidates: Stream[Point]): Stream[Point] = {
    val doorCount = Math.min(1 + Math.max(0, Random.nd02(2) - 2), 1 + candidates.size / 2)
    Random.choose(candidates, doorCount).toStream
  }
  
  private def generateSubdivMap(map: TileMap, area: Area, wall: Area,
                                maximumSize: => Int,
                                initialAngle: Int, angleVariation: => Int, 
                                doorChooser: Stream[Point] => Stream[Point], doorChance: Int,
                                wallSplitter: Area => Point): Area = {
    if (area.points.size < maximumSize) {
      map.setArea(area, Floor)
      area
    } else {
      assert(!area.points.isEmpty)
      val splitPoint = wallSplitter(wall filter (area.touches))
      assert(wall.pointSet contains splitPoint)
      val angle = initialAngle + 90 + angleVariation
      val rayStart = splitPoint.neighbours(true).filter(area.pointSet.contains).toStream.head
      assert(area.pointSet contains rayStart)
      val newWall = ThickLine.lineIn(area, rayStart, angle) ** area
      val split = area split newWall
      val rooms = split.map(generateSubdivMap(map, _, newWall, maximumSize, angle, angleVariation, doorChooser, doorChance, wallSplitter))
      val doorCandidates = newWall.points.filter(p => rooms.forall(_.touches(p, false)))
      if (doorCandidates.isEmpty) {
        // Seems that the rooms can't be connected, so take only the biggest one and fill up the others
        var biggestRoom = rooms.head
        for (room <- rooms if room.points.size > biggestRoom.points.size) biggestRoom = room
        for (room <- rooms if room != biggestRoom) map.clearArea(room)
        biggestRoom
      } else {
        val doors = Area(doorChooser(doorCandidates))
        for (door <- doors.points) {
          if (Random.next(100) < doorChance)
            map.setTile(door, Door(false))
          else
            map.setTile(door, Floor)
        }
        rooms.foldLeft(doors)(_ ++ _)
      }
    }
  }
}
