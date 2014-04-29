package roguelike.characters.ai

import roguelike.map._
import roguelike.map.path._
import roguelike.map.geometry.Point

class MonsterPathFinder(monster: Character) {
  val passabilityView = new MapView {
    def get(x: Int, y: Int) = {
      monster.map.getTile(Point(x, y)) match {
        case Door(false) => 2
        case t: Tile if t.blocking => -1
        case _ => monster.map.getCharacterAt(Point(x, y)) match {
          case None => 1
          case Some(Player) => 1
          case Some(otherMonster) if otherMonster == monster => 1
          case Some(otherMonster) if monster.location.neighbourhood(true) contains Point(x, y) => -1
          case Some(otherMonster) => Math.min((otherMonster.waitingTime / 1000).toInt, 2)
        }
      }
    }
  }

  val pathFinder = new MapPathFinder(passabilityView, true)

  def findPath(to: Point) = {
    pathFinder.findPath(monster.location, to)
  }

  def stepTowards(to: Point): Option[Action] = {
    findPath(to) match {
      case Some(loc :: step :: rest) =>
        monster.map.getTile(step) match {
          case Door(false) => Some(OpenDoor(step - monster.location))
          case _ => Some(Move(step - monster.location))
        }
      case _ => None
    }
  }
}
