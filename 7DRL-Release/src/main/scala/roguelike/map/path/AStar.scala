package roguelike.map.path

trait AStar extends PathFinder {
  type Node <: AStarNode

  var maxPathLength: Option[Int] = None
  
  trait AStarNode extends GraphNode[Node] {
    this: Node =>
    
    private[AStar] var bestDist = Int.MaxValue
    private[AStar] var cameFrom: Option[Node] = None
    
    private[AStar] var heuristic: Int = 0
    
    def calculateHeuristic(goal: Node): Int
    
    def prependPath(to: List[Node]): List[Node] = cameFrom match {
      case Some(from) => from.prependPath(this :: to)
      case None => this :: to
    }
    
    def path = prependPath(Nil)
    
    def setBestDist(dist: Int, from: Node) = if (dist < bestDist) {
      bestDist = dist
      cameFrom = Some(from)
    }
    
    def rating = bestDist + heuristic
  }
  
  def findPath(start: Node, goal: Node): Option[List[Node]] = {
    start.bestDist = 0
    start.heuristic = start.calculateHeuristic(goal)
    var openSet: List[Node] = List(start)
    var closedSet: Set[Node] = Set.empty
    while (!openSet.isEmpty) {
      val next = openSet.head
      if (next == goal) return Some(goal.path)
      openSet = openSet.tail
      closedSet += next
      if (maxPathLength == None || next.bestDist <= maxPathLength.get)
      {
        for ((cost, neighbour) <- next.edges if !(closedSet contains neighbour)) {
          neighbour.setBestDist(next.bestDist + cost, next)
          if (!(openSet contains neighbour)) {
            neighbour.heuristic = neighbour.calculateHeuristic(goal)
            openSet = neighbour :: openSet
          }
        }
        openSet = openSet.sort(_.rating < _.rating)
      }
    }
    None
  }
}
