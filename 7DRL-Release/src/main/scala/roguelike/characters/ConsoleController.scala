package roguelike.characters

import roguelike.ui.console._
import roguelike.map._
import roguelike.map.geometry._
import roguelike.combat._
import roguelike.items._
import roguelike.characters.ai._
import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}
import scala.collection.immutable.Queue

class ConsoleController(character: Humanoid) extends Controller {
  val cons = new Console(Math.max(80, Dungeon.levelWidth + 2), Dungeon.levelHeight + 4)
  cons.setTitle("Fortress of the goblin king")
  val seenLocations: MutableMap[TileMap, MutableSet[Point]] = MutableMap[TileMap, MutableSet[Point]]()
  var messageQueue: Queue[Message] = Queue.Empty
  var levelUp = false
  var plannedAction: Option[Action] = None

  // Statistics
  var killedFair = 0
  var stabbed = 0
  var shot = 0

  var showedIntroduction = false

  def endGame = {
    clearMessageView
    printMessage("Really leave the dungeon? ")
    if (cons.readCharacter == 'y') {
      if (GoblinKing.alife)
        printMessage("You cowardly left the dungeon without doing your job!")
      else
        printMessage("Congratulations, you won!\n  You stabbed " + stabbed + "and shot " + shot + " enemies, but killed " + killedFair + " in fair fight.")
      cons.readKey
      System.exit(0)
    }
  }

  override def handleMessage(msg: Message) = {
    messageQueue = messageQueue enqueue msg
    msg match {
      case DeathMessage(ch) if ch == character => {
        renderMap
        showCharacterInfo
        clearMessageView
        printMessages
        printMessage("Try again? (y/n)")
        def yesNo: Boolean = {
          cons.readCharacter match {
            case 'y' => true
            case 'n' => false
            case _ => yesNo
          }
        }
        Player.wantsRestart = yesNo
        if (!Player.wantsRestart)
          cons.close
        else {
          seenLocations.clear
          messageQueue = Queue.Empty
          levelUp = false
          stabbed = 0
          killedFair = 0
          shot = 0
          plannedAction = None
        }
      }
      case _ =>
    }
  }
  
  def act = {
    if (plannedAction.isDefined) {
      val action = plannedAction.get
      plannedAction = None
      action
    } else {
      updateSeenLocations
      doLevelUp
      renderMap
      showCharacterInfo
      clearMessageView
      printMessages

      readAction
    }
  }
  
  def readAction: Action = {
    val key = cons.readKey
    getDir(key).map(bump) getOrElse (key.getKeyCode match {
      case Key.numpad5 | Key.begin => Wait
      case Key.f6 => debug; act
      case Key.escape => {
        clearMessageView
        printMessage("Really quit? (y for yes)")
        if (cons.readCharacter == 'y') System.exit(0)
        readAction
      }
      case _ => key.getCharacter match {
        case '<' => GoUpStairs
        case '>' => GoDownStairs
        case 'g' => pickUp
        case 'd' => drop
        case 'e' => equip
        case 'r' => unEquip
        case 'x' => examine(character.location); readAction
        case 'L' => loadAmmo
        case 'a' => aim
        case 'f' => fire
        case 't' => useTorchHolder
        case 'i' => examineInventory
        case 'c' =>
          clearMessageView
          printMessage("Close door in which direction?")
          getDir(cons.readKey).map(CloseDoor) getOrElse readAction
        case '!' => character.emitMessage(Shout(character)); readAction
        case '.' => Wait
        case _ =>
          util.Debug.log("Unknown key: " + key.getModifierString + " " + key.getKeyString + " (" + key.getCharacter + ")"); readAction
      }
    })
  }

  def getDir(key: Key): Option[Point] = {
    key.getKeyCode match {
      case Key.up | Key.numpad8 | Key.keypadUp | Key.k => Some(Point(0, -1))
      case Key.down | Key.numpad2 | Key.keypadDown | Key.j => Some(Point(0, 1))
      case Key.left | Key.numpad4 | Key.keypadLeft | Key.h => Some(Point(-1, 0))
      case Key.right | Key.numpad6 | Key.keypadRight | Key.l => Some(Point(1, 0))
      // Diagonals
      case Key.numpad7 | Key.home | Key.y => Some(Point(-1, -1))
      case Key.numpad9 | Key.pageUp | Key.u => Some(Point(1, -1))
      case Key.numpad1 | Key.end | Key.b => Some(Point(-1, 1))
      case Key.numpad3 | Key.pageDown | Key.n => Some(Point(1, 1))
      case _ => None
    }
  }

  def doLevelUp = if (levelUp) {
    levelUp = false
    clearMessageView
    val msg = "You gain a new level! Please choose what to increase:\n"
    val choices: List[(String, () => Unit)] = List(
      ("Strength " + Player.strength + " -> " + (Player.strength + 1), { () => Player.strength += 1 }),
      ("Agility " + Player.agility + " -> " + (Player.agility + 1), { () => Player.agility += 1 }),
      ("Hiding " + Player.hiding + " -> " + (Player.hiding + 2), { () => Player.hiding += 2 }),
      ("Sneaking " + Player.sneaking + " -> " + (Player.sneaking + 2), { () => Player.sneaking += 2 }),
      ("Aiming " + Player.aiming + " -> " + (Player.aiming + 2), { () => Player.aiming += 2 }),
      ("Stabbing " + Player.stabbing + " -> " + (Player.stabbing + 2), { () => Player.stabbing += 2 })
    )
    val choicesWithLetters = ("abcdefghijklmnopqrstuvwxyz".toCharArray.toList) zip choices
    val choicesMsg = choicesWithLetters map { case (l, (t, _)) => "  " + l + ". " + t + "\n" } mkString ""
    printMessage(msg + choicesMsg)
    def readChoice: (String, () => Unit) = {
      val ch = cons.readCharacter
      choicesWithLetters.find(_._1 == ch).map(_._2).getOrElse(readChoice)
    }
    readChoice._2()
  }

  def debug = if (util.Debug.enabled) {
    val key = cons.readKey
    key.getKeyCode match {
      case _ => key.getCharacter match {
          case 'h' =>
            Player.maxHP = 99; Player.heal(99)
          case 'i' =>
            Player.hiding = 99; Player.sneaking = 99
          case 's' =>
            Player.strength = 99; Player.agility = 99
          case 'r' =>
            val mapSeenLocations = seenLocations.getOrElseUpdate(character.map, MutableSet[Point]())
            for (p <- Rect(Point(-1, -1), Dungeon.levelWidth + 2, Dungeon.levelHeight + 2).points) {
              mapSeenLocations += p
            }
          case 'l' =>
            util.Debug.log("Location: " + character.location)
          case _ =>
      }
    }
  }

  def debugLocation(loc: Point) = if (util.Debug.enabled) {
    util.Debug.log("Location: " + loc + " Character: " + character.map.getCharacterAt(loc))
    character.map.getCharacterAt(loc) match {
      case Some(npc: NPC) =>
        util.Debug.log("Waiting time: " + npc.waitingTime)
        npc.controller match {
          case ai: StateAI =>
            util.Debug.log("States: " + ai.states.mkString(", "))
        }
      case _ => 
    }
  }
  
  def bump(dir: Point): Action = {
    val loc = character.location + dir
    if (!lit(loc, false))
      Move(dir)
    else character.map.getCharacterAt(loc) match {
      case Some(monster) => AttackCharacter(monster)
      case None => character.map.getTile(loc) match {
          case Door(false) => OpenDoor(dir)
          case _ => Move(dir)
      }
    }
  }

  def examine(loc: Point): Unit = {
    val renderLoc = worldToView(loc)
    clearMessageView; renderMap
    cons.setBackground(renderLoc.x, renderLoc.y, Color.yellow)
    val vis = character.visibility(loc)
    vis match {
      case FullyVisible =>
      case Visible => printMessage("Shadow.")
      case NotVisible => printMessage("Darkness.")
    }
    if (vis != NotVisible || hasSeenLocation(character.map, loc)) character.map.getTile(loc) match {
      case Floor => printMessage("Floor.")
      case Wall => printMessage("Wall.")
      case Door(true) => printMessage("An open door (use 'c' to close it).")
      case Door(false) => printMessage("A closed door (bump to open it).")
      case DungeonExit => printMessage("Stairs leading out of the dungeon.")
      case UpStairs(_) => printMessage("Stairs leading up.")
      case DownStairs(_) => printMessage("Stairs leading down.")
      case t: TorchHolder => printMessage("A torch holder with" + (if (t.hasTorch) "" else "out") + " a torch.")
      case _ =>
    }
    if (vis != NotVisible) {
      val items = character.map.items.filter(_.location == loc)
      if (!items.isEmpty)
        printMessage("Items: " + items.map(_.description).mkString(", ") + ".")
      character.map.getCharacterAt(loc) match {
        case Some(Player) => printMessage("You.")
        case Some(ch: HasInventory) if ch.controller.isInstanceOf[StateAI] =>
          ch.controller.asInstanceOf[StateAI].states.head match {
            case gcs: GuardCharacterState =>
              printMessage(ch.description + " there is guarding " + gcs.guardedCharacter.description + ".")
            case _: WaitState | _: GuardLocationState => printMessage(ch.description + " there is just standing around.")
            case _: AlertWaitState => printMessage(ch.description + " there is looking around observantly.")
            case _: FollowAndAttackPlayerState =>
              if (ch.location.neighbours(true) contains character.location)
                printMessage(ch.description + " there is attacking you.")
              else
                printMessage(ch.description + " there is moving towards you.")
            case GoToLocationState(targetLocation) => 
              if (targetLocation != ch.location)
                printMessage(ch.description + " there is moving " + direction(targetLocation - ch.location) + "wards.")
              else
                printMessage(ch.description + " there is moving around.")
            case InvestigateLocationState(_) =>
              printMessage(ch.description + " there is looking around suspiciously.")
            case PatrolState(loc :: Nil) =>
              printMessage(ch.description + " there is just standing around.")
            case ps @ PatrolState(_) =>
              val targetLocation = ps.currentTarget
              if (targetLocation != ch.location)
                printMessage(ch.description + " there is moving " + direction(targetLocation - ch.location) + "wards.")
              else
                printMessage(ch.description + " there is moving around.")
            case _ => printMessage(ch.description + ".")
          }
          if (!ch.equippedItems.isEmpty) {
            printMessage("He has equipped: " + ch.equippedItems.map(_.description).mkString(", ") + ".")
          }
        case None =>
      }
    }
    val key = cons.readKey
    getDir(key) match {
      case Some(dir) => examine(loc + dir)
      case None => key.getCharacter match {
          case 'd' => debugLocation(loc)
          case _ => renderMap
      }
    }
  }

  def examineInventory: Action = {
    clearMessageView; renderMap
    val itemPairs = character.inventory zip ("abcdefghijklmnopqrstuvwxyz".toCharArray.toList)
    printMessage("Inventory:\n" + itemPairs.map(p => "  " + p._2 + ". " + p._1.description).mkString("\n"))
    val key = cons.readKey
    itemPairs.find(_._2 == key.getCharacter).flatMap(p => examineItem(p._1)) getOrElse { printMessage("Okay then."); readAction }
  }

  def examineItem(item: Item): Option[Action] = {
    clearMessageView; renderMap
    printMessage(item.longDescription)
    if (character.equipped(item)) printMessage("(Equipped)")
    var actions: List[(Char, String, Item => Action)] = Nil
    if (character.canEquip(item) && !character.equipped(item)) actions ::= (('e', "equip", Equip))
    if (character.equipped(item)) actions ::= (('r', "remove", UnEquip))
    actions ::= (('d', "drop", Drop))
    if (character.ranged.isDefined && canBeFired(item))
      actions ::= (('l', "load your " + character.ranged.get.description, it => LoadRangedWeapon(character.ranged.get, character.removeOne(it).asInstanceOf[Projectile])))
    if (Some(item) == character.ranged) {
      actions ::= (('l', "load", it => loadAmmo))
      actions ::= (('a', "aim", it => aim))
      actions ::= (('f', "fire", it => fire))
    }
    printMessage("Press " + actions.map(a => "'" + a._1 + "' to " + a._2).mkString(", ") + ".")
    val key = cons.readKey
    actions.find(_._1 == key.getCharacter).map(a => a._3(item))
  }

  def useTorchHolder: Action = {
    character.currentTile match {
      case t: TorchHolder if t.hasTorch => TakeTorch
      case t: TorchHolder if !t.hasTorch => {
        val torch = character.inventory.find(_.isInstanceOf[Torch])
        (for (t <- torch) yield PutTorch(t.asInstanceOf[Torch])) getOrElse {printMessage("You have no torch."); readAction}
      }
      case _ => printMessage("What?"); readAction
    }
  }

  def aim: Action = {
    if (character.ranged == None) {
      printMessage("You have no ranged weapon equipped!")
      readAction
    } else if (character.loadedAmmo == None) {
      chooseTarget.map { target =>
        plannedAction = Some(AimRangedWeapon(target))
        loadAmmo
      } getOrElse { printMessage("Okay then."); readAction }
    } else {
      chooseTarget.map(AimRangedWeapon) getOrElse { printMessage("Okay then."); readAction }
    }
  }

  def fire: Action = {
    if (character.ranged == None) {
      printMessage("You have no ranged weapon equipped!")
      readAction
    } else if (character.loadedAmmo == None) {
      chooseTarget.map { target =>
        plannedAction = Some(FireRangedWeapon(target))
        loadAmmo
      } getOrElse { printMessage("Okay then."); readAction }
    } else {
      chooseTarget.map(FireRangedWeapon) getOrElse { printMessage("Okay then."); readAction }
    }
  }

  def chooseTarget: Option[Character] = {
    if (character.lastAiming._2 != null && character.canFireAt(character.lastAiming._2)) {
      printMessage("Press space to target " + character.lastAiming._2.description + " again or the letter of your new target.")
    } else {
      printMessage("Press the letter of your target.")
    }
    val tp = ("abcdefghijklmnopqrstuvwxyz".toCharArray.toList) zip (character.map.characters filter character.canFireAt)
    val targetPairs = if (character.lastAiming._2 != null) (' ', character.lastAiming._2) :: tp else tp
    for ((ch, target) <- targetPairs) {
      val viewLoc = worldToView(target.location)
      cons.setCharacter(viewLoc.x, viewLoc.y, ch)
    }
    val ch = cons.readCharacter
    targetPairs.find(_._1 == ch).map(_._2)
  }

  def loadAmmo: Action = {
    val itemPairs = (character.inventory filter canBeFired) zip ("abcdefghijklmnopqrstuvwxyz".toCharArray.toList)
    if (itemPairs.isEmpty) {
      printMessage("You have no suitable ammunition!")
      readAction
    } else if (itemPairs.size == 1) {
      LoadRangedWeapon(character.ranged.get, character.removeOne(itemPairs.head._1).asInstanceOf[Projectile])
    } else {
      val msg = "Use which projectile?\n" + itemPairs.map(p => "  " + p._2 + ". " + p._1.description + "\n").mkString("")
      clearMessageView
      printMessage(msg)
      val ch = cons.readCharacter
      itemPairs.find(_._2 == ch).map(p => LoadRangedWeapon(character.ranged.get, character.removeOne(p._1).asInstanceOf[Projectile])).getOrElse({ printMessage("Okay then..."); readAction })
    }
  }

  private def canBeFired(item: Item) = item match {
    case ItemStack((sample: Projectile) :: _) => character.ranged.exists(_.fires(sample))
    case p: Projectile => character.ranged.exists(_.fires(p))
    case _ => false
  }
  
  def pickUp = {
    val items = character.map.items.filter(_.location == character.location)
    if (items.isEmpty) {
      printMessage("There are no items here.")
      readAction
    } else if (items.size == 1) {
      PickUp(items.head)
    } else {
      clearMessageView
      val itemPairs = items zip ("abcdefghijklmnopqrstuvwxyz".toCharArray.toList)
      val msg = "Pick up which item?\n" + itemPairs.map(p => "  " + p._2 + ". " + p._1.description + "\n").mkString("")
      printMessage(msg)
      val ch = cons.readCharacter
      itemPairs.find(_._2 == ch).map(p => pickUpItem(p._1)).getOrElse(readAction)
    }
  }

  private def pickUpItem(it: Item): Action = {
    if (it.big && character.bigItems.size >= character.bigItemLimit) {
      printMessage("You cannot carry this without first dropping " + character.bigItems.head.description + "!")
      readAction
    }
    else
      PickUp(it)
  }
  
  def drop = {
    val itemPairs = character.inventory zip ("abcdefghijklmnopqrstuvwxyz".toCharArray.toList)
    val msg = "Drop which item?\n" + itemPairs.map(p => "  " + p._2 + ". " + p._1.description + "\n").mkString("")
    clearMessageView
    printMessage(msg)
    val ch = cons.readCharacter
    itemPairs.find(_._2 == ch).map(p => Drop(p._1)).getOrElse(readAction)
  }
  
  def equip = {
    val itemPairs = (character.inventory filter character.canEquip) zip ("abcdefghijklmnopqrstuvwxyz".toCharArray.toList)
    val msg = "Equip which item?\n" + itemPairs.map(p => "  " + p._2 + ". " + p._1.description + "\n").mkString("")
    clearMessageView
    printMessage(msg)
    val ch = cons.readCharacter
    itemPairs.find(_._2 == ch).map(p => Equip(p._1)).getOrElse(readAction)
  }

  def unEquip = {
    val itemPairs = (character.inventory filter character.equipped) zip ("abcdefghijklmnopqrstuvwxyz".toCharArray.toList)
    val msg = "Unequip which item?\n" + itemPairs.map(p => "  " + p._2 + ". " + p._1.description + "\n").mkString("")
    clearMessageView
    printMessage(msg)
    val ch = cons.readCharacter
    itemPairs.find(_._2 == ch).map(p => UnEquip(p._1)).getOrElse(readAction)
  }
  
  def printMessages = {
    if (!showedIntroduction) {
      showedIntroduction = true
      printMessage("Welcome! Your goal is to get to the sixth level of the dungeon and kill the goblin king, then get back here.")
      printMessage("Use the vi or arrow keys or the numpad to move around, and '.' or Numpad-5 to wait. You can examine your environment and items with 'x' and 'i'.")
    }
    for (msg <- messageQueue) {
      printMessage(msg)
    }
    messageQueue = Queue.Empty
    val itemsAtCurrentLocation = character.map.items.filter(_.location == character.location)
    if (!itemsAtCurrentLocation.isEmpty) {
      val msg = "On the floor: " + itemsAtCurrentLocation.map(_.description).mkString(", ") + " (use 'g' to pick something up)."
      printMessage(msg)
    }
    character.currentTile match {
      case DownStairs(_) => printMessage("There are stairs going down here ('>' to use).")
      case UpStairs(_) => printMessage("There are stairs going up here ('<' to use).")
      case DungeonExit => printMessage("There are stairs leading out of the dungeon here ('<' to use).")
      case t: TorchHolder => printMessage("There's torch holder with" + (if (t.hasTorch) "" else "out") + " a torch here (use 't' to " + (if (t.hasTorch) " remove the torch)." else " put a torch into it)."))
      case _ =>
    }
  }

  def printMessage(msg: Message): Unit = msg match {
    case PickUpMessage(ch, it) if ch == character => printMessage("You pick up the " + it.description + ".")
    case PickUpMessage(monster, it) => printMessage(monster.description.capitalize + " picks up " + it.description + ".")
    case DropMessage(ch, it) if ch == character => printMessage("You drop the " + it.description + ".")
    case DropMessage(monster, it) => printMessage(monster.description.capitalize + " drops " + it.description + ".")
    case TakeTorchMessage(ch) if ch == character => printMessage("You remove the torch from the holder.")
    case PutTorchMessage(ch) if ch == character => printMessage("You put a torch in the holder.")
    case AttackMessage(ch, mons, true, _, _, _) if ch == character =>
      printMessage("You attack " + mons.description + ", but he blocks the attack.")
    case AttackMessage(mons, ch, true, _, _, _) if ch == character =>
      printMessage(mons.description + " attacks you, but you block the attack.")
    case AttackMessage(ch, mons, false, true, _, _) if ch == character =>
      printMessage("You attack " + mons.description + ", but he evades the attack.")
    case AttackMessage(mons, ch, false, true, _, _) if ch == character =>
      printMessage(mons.description + " attacks you, but you evade the attack.")
    case AttackMessage(ch, mons, false, false, true, _) if ch == character =>
      printMessage("You attack " + mons.description + ", but his armour protects him.")
    case AttackMessage(mons, ch, false, false, true, _) if ch == character =>
      printMessage(mons.description + " attacks you, but your armour protects you.")
    case AttackMessage(ch, mons, false, false, false, stab) if ch == character =>
      if (!mons.alife) (if (stab) stabbed += 1 else killedFair += 1)
      printMessage("You " + (if (stab) "stab " else "hit ") + mons.description + "!")
    case AttackMessage(mons, ch, false, false, false, stab) if ch == character =>
      printMessage(mons.description + (if (stab) " stab" else " hit") + "s you!")
    case LoadRangedWeaponMessage(ch) if ch == character && ch.ranged.isDefined =>
      printMessage("You load your " + character.ranged.get.description + ".")
    case LoadRangedWeaponMessage(mons) if mons.ranged.isDefined =>
      printMessage(mons.description + " loads his " + mons.ranged.get.description + ".")
    case AimRangedWeaponMessage(ch, target) if ch == character =>
      printMessage("You got a good aim at " + target.description + "!")
    case FireRangedWeaponMessage(ch, target) if ch == character =>
      printMessage("You fire at " + target.description + ".")
    case FireRangedWeaponMessage(mons, target) if target == character =>
      printMessage(mons.description + " fires at you.")
    case HitByProjectileMessage(ch, projectile, true, _) if ch == character =>
      printMessage("You block the " + projectile.description + ".")
    case HitByProjectileMessage(mons, projectile, true, _) =>
      printMessage(mons.description + " blocks the " + projectile.description + ".")
    case HitByProjectileMessage(ch, projectile, false, true) if ch == character =>
      printMessage("You are hit by an " + projectile.description + ", but your armour protects you.")
    case HitByProjectileMessage(mons, projectile, false, true) =>
      printMessage(mons.description + " is hit by " + projectile.description + ", but his armour protects him.")
    case HitByProjectileMessage(ch, projectile, false, false) if ch == character =>
      printMessage("You are hit by an " + projectile.description + "!")
    case HitByProjectileMessage(mons, projectile, false, false) =>
      if (!mons.alife) shot += 1
      printMessage(mons.description + " is hit by " + projectile.description + "!")
    case DeathMessage(ch) =>
      if (ch == character) printMessage("You die...", Color.red) else printMessage(ch.description + " dies.")
    case BumpMessage(ch, location, false) if ch == character =>
      printMessage("You bump into a wall.")
    case BumpMessage(ch, location, true) if ch == character =>
      printMessage("You bump into someone.")
    case OpenDoorMessage(ch, _) if ch == character =>
      printMessage("You open the door.")
    case OpenDoorMessage(ch, _) =>
      printMessage(ch.description + " opens the door.")
    case CloseDoorMessage(ch, _) if ch == character =>
      printMessage("You close the door.")
    case CloseDoorMessage(ch, _) =>
      printMessage(ch.description + " closes the door.")
    case Shout(ch) if ch == character =>
      printMessage("You shout!")
    case Shout(ch) =>
      printMessage(ch.description + " shouts!")
    case Noise(origin) =>
      val noiseDesc = msg match {
        case _: Shout#Noise => "a shout"
        case _: ClangingNoise => "a clanging noise"
        case _: StepNoise => "steps"
        case _ => "a noise"
      }
      printMessage("You hear " + noiseDesc + " from the " + direction(origin - character.location) + ".")
    case UseStairsMessage(ch, _) if ch == character =>
      printMessage("You use the stairs.")
    case ComeFromStairsMessage(mons) if mons != character => mons.currentTile match {
        case UpStairs(_) => printMessage(mons.description + " comes down the stairs.")
        case DownStairs(_) => printMessage(mons.description + " comes up the stairs.")
    }
    case StairsBlockedMessage(ch) if ch == character =>
      printMessage("The stairs are blocked!")
    case MoveMessage(_, _, _) =>
    case _ =>
      util.Debug.log("Received, but didn't show: " + msg)
  }

  def direction(delta: Point): String = {
    val y = if (delta.x != 0) delta.y / Math.abs(delta.x) else delta.y
    val x = if (delta.y != 0) delta.x / Math.abs(delta.y) else delta.x
    val northSouth = if (y < 0) "north" else if (y > 0) "south" else ""
    val eastWest = if (x < 0) "west" else if (x > 0) "east" else ""
    util.Debug.log("delta=" + delta + " => " + x + "/" + y + " => " + northSouth + eastWest)
    northSouth + eastWest
  }
  
  def printMessage(msg: String, color: Color): Unit = {
    cons.setForeground(color)
    cons.print(msg.capitalize + " ")
  }

  def printMessage(msg: String): Unit = printMessage(msg, Color.white)
  
  def clearMessageView = {
    for (x <- 0 until cons.getWidth; y <- 0 until cons.getHeight - 2) {
      if (y < 2 || x < (cons.getWidth - Dungeon.levelWidth) / 2 - 1 || x >= (cons.getWidth - Dungeon.levelWidth) / 2 + Dungeon.levelWidth + 1)
      {
        cons.setCharacter(x, y, ' ')
        cons.setBackground(x, y, Color.black)
        cons.setForeground(x, y, Color.white)
      }
    }
    cons.setCursor(0, 0)
  }
  
  def showCharacterInfo = {
    for (x <- 0 until cons.getWidth; y <- (cons.getHeight - 2) until (cons.getHeight)) {
      cons.setCharacter(x, y, ' ')
      cons.setBackground(x, y, Color.black)
      cons.setForeground(x, y, Color.white)
    }
    val depthStr = Dungeon.getDepthOfMap(character.map).map("D: " + _ + " ").getOrElse("")
    val firstLine = (depthStr +
                     "HP: " + character.hp +
                     " STR: " + character.strength +
                     " AGI: " + character.agility)
    for ((c, x) <- firstLine.toCharArray.zipWithIndex) {
      cons.setCharacter(x, cons.getHeight - 2, c)
    }
    val secondLine = "Equipped: " + (if (character.equippedItems.isEmpty) "Nothing" else character.equippedItems.map(_.description).mkString(", "))
    val bigItems = if (character.bigItems.isEmpty) "" else "; Carrying " + character.bigItems.map(_.description).mkString(", ")
    val loaded = if (character.loadedAmmo.isEmpty) "" else "; Ammo: " + character.loadedAmmo.map(_.description).mkString
    for ((c, x) <- (secondLine + bigItems + loaded).toCharArray.zipWithIndex) {
      if (x < cons.getWidth)
        cons.setCharacter(x, cons.getHeight - 1, c)
    }
  }
  
  def hasSeenLocation(m: TileMap, p: Point): Boolean = {
    seenLocations.get(m).map(_ contains p).getOrElse(false)
  }
  
  def updateSeenLocations = {
    val mapSeenLocations = seenLocations.getOrElseUpdate(character.map, MutableSet[Point]())
    if (mapSeenLocations.isEmpty && seenLocations.size > 1) levelUp = true // New map, new level!
    for (msg <- messageQueue) msg match {
      case BumpMessage(ch, location, false) if ch == character =>
        mapSeenLocations += location
      case _ =>
    }
    for (p <- Rect(Point(-1, -1), Dungeon.levelWidth + 2, Dungeon.levelHeight + 2).points) {
      if (lit(p, false)) {
        mapSeenLocations += p
      }
    } 
  }

  def lit(p: Point, fully: Boolean): Boolean = {
    val visibility = character.visibility(p)
    if (visibility == FullyVisible)
      true
    else if (visibility == NotVisible)
      false
    else
      !fully
  }

  def worldToView(p: Point) = {
    val topLeftCorner = Point((cons.getWidth - Dungeon.levelWidth) / 2 - 1, 2) + Point(1, 0)
    topLeftCorner + p
  }
  
  def renderMap = {
    def renderTile(p: Point, tile: Tile, penumbra: Boolean, fromMemory: Boolean) = {
      val glyph = tile match {
        case Floor => '.'
        case Wall => '#'
        case DownStairs(_) => '>'
        case UpStairs(_) | DungeonExit => '<'
        case Door(true) => '-'
        case Door(false) => '+'
        case _: TorchHolder => '*'
      }
      val color = tile match {
        case Door(_) => Color.brown
        case _ => Color.white
      }
      cons.setCharacter(p.x, p.y, glyph)
      cons.setBackground(p.x, p.y, Color.black)
      cons.setForeground(p.x, p.y, if (fromMemory) Color.darkGray.darker.darker else if (penumbra) color.darker.darker else color)
    }
    def renderCharacter(p: Point, ch: Character, penumbra: Boolean) = {
      val glyph = ch match {
        case Player => '@'
        case _: Goblin => 'g'
      }
      val color = ch match {
        case _: GoblinGuard => Color.green.darker.darker
        case _: GoblinElite => Color.yellow.darker
        case GoblinKing => Color.red.darker
        case _: Goblin => Color.green
        case _ => Color.white
      }
      cons.setCharacter(p.x, p.y, glyph)
      cons.setBackground(p.x, p.y, Color.black)
      cons.setForeground(p.x, p.y, if (penumbra) Color.gray else color)
    }
    def renderItem(p: Point, it: Item, penumbra: Boolean) {
      cons.setCharacter(p.x, p.y, it.glyph)
      cons.setBackground(p.x, p.y, Color.black)
      cons.setForeground(p.x, p.y, if (penumbra) Color.gray else Color.white)
    }
    
    val characterMap = Map((for (ch <- character.map.characters) yield (ch.location, ch)): _*)
    val itemMap = Map((for (it <- character.map.items) yield (it.location, it)): _*)
    
    for (p <- Rect(Point(-1, 0), Dungeon.levelWidth + 2, Dungeon.levelHeight).points) {
      val viewLoc = worldToView(p)
      if (!lit(p, false)) {
        if (hasSeenLocation(character.map, p)) {
          renderTile(viewLoc, character.map.getTile(p), false, true)
        } else {
          cons.setCharacter(viewLoc.x, viewLoc.y, ' ')
          cons.setBackground(viewLoc.x, viewLoc.y, Color.black)
        }
      } else {
        val penumbra = !lit(p, true)
        (characterMap.get(p), itemMap.get(p)) match {
          case (Some(ch), _) => renderCharacter(viewLoc, ch, penumbra)
          case (None, Some(it)) => renderItem(viewLoc, it, penumbra)
          case (None, None) => renderTile(viewLoc, character.map.getTile(p), penumbra, false)
        }
      }
    } 
  }
}
