package roguelike

import roguelike.map.geometry.Point
import roguelike.map.{Dungeon, DungeonExit}
import roguelike.characters.Player
import roguelike.items._

object Roguelike {
  def main(args: Array[String]): Unit = {
    run
  }

  def run: Unit = {
    reset

    while (!TimeLine.isEmpty && Player.alife && Player.map != null) {
      TimeLine.step
    }

    if (Player.wantsRestart) run
  }

  def reset = {
    Dungeon.reset
    Player.reset
    TimeLine.clear
    Player.changeMap(Dungeon.getLevel(1))
    Player.location = Dungeon.monsterFreeLocations(Player.map).first
    Player.map.setTile(Player.location, DungeonExit)
    Player addToInventory new Dagger
    Player addToInventory new LeatherArmour
    Player.inventory.foreach(Player.equip)
    Player addToInventory new Torch
    Player addToInventory new ItemStack(5, () => new Arrow)
    Player addToInventory new Bow
    Player.comeToLife
  }
}
