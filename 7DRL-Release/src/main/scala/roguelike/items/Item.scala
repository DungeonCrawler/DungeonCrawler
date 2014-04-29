package roguelike.items

import roguelike.map.geometry.Point
import roguelike.map.TileMap
import roguelike.characters._

trait Item {
  def glyph: Char
  def description: String // TODO: There should be a way to add the right article; right now this is hacked together
  def longDescription: String

  def big = false
  
  var location = Point(0, 0)
  private var _map: Option[TileMap] = None
  def map = _map
  
  def changeMap(m: Option[TileMap]): Unit = { // TODO: Make consistent with Character, i.e. changeMap(TileMap) + leaveMap
    if (_map.isDefined) _map.get.items -= this
    if (m.isDefined) m.get.items ::= this
    _map = m
  }
}

trait BigItem extends Item {
  override def big = true
}

case class ItemStack(val items: List[Item]) extends Item {
  def this(count: Int, gen: () => Item) = this(List.make(count, gen).map(_()))

  def glyph = items.head.glyph

  def take = if (items.tail.isEmpty) ((items.head, None)) else ((items.head, Some(ItemStack(items.tail))))

  def canAdd(item: Item): Boolean = item match {
    case ItemStack(it :: _) => canAdd(it)
    case _ => item.description == items.head.description // HACK!
  }

  def +(item: Item): ItemStack = item match {
    case ItemStack(items) => items.foldLeft(this)(_ + _)
    case _ => ItemStack(item :: items)
  }

  def description = if (items.size == 1) items.head.description else (items.size + " " + items.head.description + "s")
  def longDescription = description + "."
}

trait Corpse extends BigItem {
  def glyph = '%'
  def description = "corpse"
  def longDescription = "A " + description + ". It's a big item - you can only carry one around at a time."
}

trait Weapon extends Item with combat.Attack {
  def glyph = ')'
  def twoHanded = false
}

trait TwoHandedWeapon extends Weapon {
  override def twoHanded = true
}

class Dagger extends Weapon with combat.Parry {
  def description = "dagger"
  def longDescription = "A dagger. Good for stabbing, not that good for fair combat."
  
  def maxStrength = 3
  def maxAgility = 12
  def effects = damage(95, 10)
}

class Sword extends Weapon with combat.Parry {
  def description = "sword"
  def longDescription = "A sword. Good for normal combat, but not as good for stabbing as a dagger."

  def maxStrength = 8
  def maxAgility = 8
  def effects = damage(90, 35, 10)
}

class Axe extends Weapon {
  def description = "axe"
  def longDescription = "An axe. It does more damage than a sword, but you can't parry with it."

  def maxStrength = 11
  def maxAgility = 6
  def effects = damage(70, 60, 20)
}

class Warhammer extends TwoHandedWeapon with BigItem {
  def description = "warhammer"
  def longDescription = "A warhammer. It does big amounts of damage, but it is two-handed and you can't parry with it."

  def maxStrength = 20
  def maxAgility = 4
  def effects = damage(80, 90, 70, 30)
}

trait RangedWeapon extends SideArm with combat.Ranged {
  def glyph = ')'
}

class Bow extends RangedWeapon {
  def description = "bow"
  def longDescription = "A bow. Very deadly, especially if you use a turn to aim at an enemy; but you need arrows, and the enemies will probably notice you."

  override def twoHanded = true
  
  def fires(ammo: combat.Projectile) = ammo.isInstanceOf[Arrow]

  def loadingTime = 1000
}

trait Ammunition extends Item with combat.Projectile {
  def glyph = '('
}

class Arrow extends Ammunition {
  def description = "arrow"
  def longDescription = "An arrow."

  def effects = damage(100, 60, 20)
}

class Shield extends SideArm with combat.Parry {
  def description = "shield"
  def longDescription = "A shield. You can even block arrows with it!"

  def glyph = '['

  def maxAgility = 11

  override def canBlockProjectiles = true
}

trait SideArm extends Item {
  def twoHanded = false
}

trait BodyArmour extends Item with combat.Armour {
  def glyph = '['

  // TODO: maxAgility
}

class LeatherArmour extends BodyArmour {
  def description = "leather armour"
  def longDescription = "A leather armour."
  
  def prevention = preventDamage(75, 10)
}
