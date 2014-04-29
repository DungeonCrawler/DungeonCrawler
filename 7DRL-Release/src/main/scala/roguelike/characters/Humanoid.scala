package roguelike.characters

import roguelike.items._

trait Humanoid extends HasInventory {
  def stabResistance = 10

  var weapon: Option[Weapon] = None
  var sideArm: Option[SideArm] = None
  var armour: Option[BodyArmour] = None
  
  override def dropped(item: Item) {
    if (weapon == Some(item)) weapon = None
    if (armour == Some(item)) armour = None
  }
  
  override def canEquip(item: Item) = super.canEquip(item) || (item match {
    case _: Weapon | _: BodyArmour | _: SideArm => true
    case _ => false
  })

  override def equip(item: Item) = item match {
    case w: Weapon =>
      weapon.foreach(unEquip)
      if (sideArm.exists(_.twoHanded) || w.twoHanded) sideArm.foreach(unEquip)
      weapon = Some(w)
    case s: SideArm =>
      sideArm.foreach(unEquip)
      if (weapon.exists(_.twoHanded) || s.twoHanded) weapon.foreach(unEquip)
      sideArm = Some(s)
    case a: BodyArmour => armour.foreach(unEquip); armour = Some(a)
    case _ => util.Debug.log("Trying to equip: " + item); super.equip(item)
  }
  
  override def unEquip(item: Item) = item match {
    case w: Weapon => 
      if (weapon == ranged) {
        loadedAmmo foreach {
          case ammo: Item => addToInventory(ammo)
          case _ =>
        }
        loadedAmmo = None
      }
      weapon = None
    case s: SideArm => 
      if (sideArm == ranged) {
        loadedAmmo foreach {
          case ammo: Item => addToInventory(ammo)
          case _ =>
        }
        loadedAmmo = None
      }
      sideArm = None
    case a: BodyArmour => armour = None
    case _ => super.unEquip(item)
  }

  override def equippedItems = {
    var e = super.equippedItems
    e = if (weapon.isDefined) weapon.get :: e else e
    e = if (sideArm.isDefined) sideArm.get :: e else e
    e = if (armour.isDefined) armour.get :: e else e
    e
  }
  
  def attack = weapon
  def parry = sideArm match {
    case Some(p: combat.Parry) => Some(p)
    case _ => weapon match {
      case Some(p: combat.Parry) => Some(p)
      case _ => None
    }
  }
  def ranged = sideArm match {
    case Some(p: combat.Ranged) => Some(p)
    case _ => weapon match {
      case Some(p: combat.Ranged) => Some(p)
      case _ => None
    }
  }
  def armours = armour.toList
}

case class Equip(item: Item) extends Action {
  def duration = 1500
  
  def valid(character: Character) = character match {
    case inv: HasInventory => (inv.inventory contains item) && (inv canEquip item)
    case _ => false
  }
  
  def execute(character: Character) = {
    val inv = character.asInstanceOf[HasInventory]
    inv equip item
  }
}

case class UnEquip(item: Item) extends Action {
  def duration = 800

  def valid(character: Character) = character match {
    case inv: HasInventory => (inv.inventory contains item) && (inv equipped item)
    case _ => false
  }

  def execute(character: Character) = {
    val inv = character.asInstanceOf[HasInventory]
    inv unEquip item
  }
}
