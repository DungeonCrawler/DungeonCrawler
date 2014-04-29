package roguelike.map.path

import scala.collection.mutable.Map

import geometry.Point

class MapPathFinder(val map: MapView, val walkDiagonals: Boolean) extends AStar {
  private val nodeMap = Map.empty[Point, Node]
  
  final case class Node(loc: Point) extends AStarNode {
    def calculateHeuristic(goal: Node) = loc dist goal.loc
    
    def edges = {
      for (l <- loc.neighbours(false); (cost, node) <- getNode(l)) yield (cost * 2, node)
    }.toList ++ (if (walkDiagonals) {
      for (l <- loc.diagonalNeighbours; (cost, node) <- getNode(l)) yield (cost * 3, node)
    } else Nil)
    
    override def toString = loc.toString
  }
  
  private def getNode(loc: Point) = {
    val cost = map.get(loc.x, loc.y)
    if (cost < 0) None else Some((cost, nodeMap.getOrElseUpdate(loc, Node(loc))))
  }
  
  def findPath(start: Point, goal: Point): Option[List[Point]] = {
    nodeMap.clear
    for ((_, startNode) <- getNode(start); 
         (_, goalNode) <- getNode(goal); 
         path <- findPath(startNode, goalNode))
      yield path.map(_.loc)
  }
}
