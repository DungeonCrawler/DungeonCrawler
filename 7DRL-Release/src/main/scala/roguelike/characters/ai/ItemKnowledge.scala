package roguelike.characters.ai

import roguelike.map.geometry.Point
import roguelike.items.Item

object ItemKnowledge {
  private var knownItems: Set[(Point, Item)] = Set.empty

  def add(item: Item) = knownItems += ((item.location, item))

  def contains(item: Item) = knownItems contains ((item.location, item))
}
