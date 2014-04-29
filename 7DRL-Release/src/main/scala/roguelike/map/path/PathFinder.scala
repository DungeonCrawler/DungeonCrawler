package roguelike.map.path

trait GraphNode[+GN <: GraphNode[GN]] {
  this: GN =>
  def edges: List[(Int, GN)]
}

trait PathFinder {
  type Node <: GraphNode[Node]
  
  def findPath(start: Node, goal: Node): Option[List[Node]]
}
