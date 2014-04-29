package roguelike.characters

import roguelike.items._

trait HasInventory extends Character {
  var inventory: List[Item] = Nil

  override def die = {
    if (alife) {
      for (e <- equippedItems) unEquip(e)
      val items = inventory
      inventory = Nil
      for (i <- items) {
        dropped(i)
        i.location = location
        i.changeMap(Some(map))
      }
    }
    super.die
  }
  
  def pickedUp(item: Item) = { }
  def dropped(item: Item) = { }
  
  def canEquip(item: Item) = false
  def equip(item: Item) = { assert(false) }
  def equipped(item: Item) = equippedItems contains item
  def unEquip(item: Item) = { assert(false) }
  def equippedItems: List[Item] = Nil

  def bigItems = inventory.filter(_.big)
  def bigItemLimit = 1
  
  def addToInventory(item: Item): Unit = {
    inventory.find(stackCanAdd(item)).map(addToStack(item)).getOrElse(inventory ::= item)
  }
  private def stackCanAdd(item: Item)(stack: Item): Boolean = stack match {
    case st: ItemStack => st.canAdd(item)
    case _ => false
  }
  private def addToStack(item: Item)(stack: Item) = {
    val newStack = stack.asInstanceOf[ItemStack] + item
    inventory -= stack
    addToInventory(newStack)
  }

  def removeOne(item: Item): Item = item match {
    case ItemStack(it :: rest) => {
      if (rest.isEmpty)
        inventory -= item
      else
        inventory = ItemStack(rest) :: (inventory - item)
      it
    }
    case _ => {
      inventory -= item
      item
    }
  }

  def addAndEquip(item: Item) = {
    addToInventory(item)
    if (canEquip(item)) equip(item)
  }

  override def hidingModifier = super.hidingModifier + {
    (if (bigItems.isEmpty) 0 else -3)
  }

  override def sneakingModifier = super.sneakingModifier + {
    (if (bigItems.isEmpty) 0 else -3)
  }

  override def lightSources = equippedItems.flatMap {
    case ils: ItemLightSource => List(ils.getLightSource(this))
    case _ => Nil
  } ::: super.lightSources
}

case class PickUp(item: Item) extends Action {
  def duration = 1000
  
  def valid(character: Character) = character match {
    case inv: HasInventory =>
      item.map.isDefined &&
      item.map.get == character.map &&
      item.location == character.location &&
      (!item.big || inv.bigItems.size < inv.bigItemLimit)
    case _ => false
  }
  
  def execute(character: Character) = {
    val inv = character.asInstanceOf[HasInventory]
    item.changeMap(None)
    inv addToInventory item
    inv.pickedUp(item)
    inv.emitMessage(PickUpMessage(character, item))
  }
}

case class Drop(item: Item) extends Action {
  def duration = 700
  
  def valid(character: Character) = character match {
    case inv: HasInventory => inv.inventory contains item
    case _ => false
  }
  
  def execute(character: Character) = {
    val inv = character.asInstanceOf[HasInventory]
    item.location = character.location
    item.changeMap(Some(character.map))
    inv.inventory -= item
    inv.dropped(item)
    inv.emitMessage(DropMessage(character, item))
  }
}

case class PickUpMessage(character: Character, item: Item) extends Message
case class DropMessage(character: Character, item: Item) extends Message
