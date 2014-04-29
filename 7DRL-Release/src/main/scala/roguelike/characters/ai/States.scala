package roguelike.characters.ai

import roguelike.combat._
import roguelike.map.{UpStairs, DownStairs}
import roguelike.map.geometry.Point
import roguelike.items.{Item, Corpse, ItemStack}
import roguelike.util.Random

class WaitState extends AIState {
  override def act: Action = {
    Wait
  }

  override def attentive = false

  override def toString = "Wait"
}

class AlertWaitState extends AIState {
  override def act: Action = Wait

  override def toString = "AlertWait"
}

case class PatrolState(locations: List[Point]) extends AIState {
  var currentTargetIndex = 0

  override def attentive = false

  override def act: Action = {
    if (character.location == currentTarget) {
      Random.maybe(50, Wait) or {
        currentTargetIndex = (currentTargetIndex + 1) % locations.size
        step
      }
    } else
      step
  }

  def step = {
    pathFinder.stepTowards(currentTarget) getOrElse {
      currentTargetIndex = (currentTargetIndex + 1) % locations.size // Try next target, but if that is blocked too, wait a turn
      pathFinder.stepTowards(currentTarget) getOrElse Wait
    }
  }

  def currentTarget = locations(currentTargetIndex)

  override def toString = locations.mkString("Patrol(", ", ", ")")
}

class AlwaysReturnToLocationState(val location: Point) extends AIState {
  override def act: Action = {
    if (location != character.location)
      push(new GoToLocationState(location) with LookOutForPlayer)
    else
      Wait
  }
}

class GuardLocationState(location: Point) extends AlwaysReturnToLocationState(location) with LookOutForPlayer {
  override def attentive = false

  override def toString = "Guard " + location
}

class GuardCharacterState(location: Point, val guardedCharacter: Character) extends GuardLocationState(location) {
  override def act: Action = {
    if (!guardedCharacter.alife) {
      foundCorpseAction
    } else {
      super.act
    }
  }
}

trait MayStop extends AIState {
  private var stopProbability = 10

  def stopChance(percent: Int) = {
    stopProbability = percent
    this
  }

  override def act: Action = {
    Random.maybe(stopProbability, pop) or super.act
  }

  override def toString = super.toString + " + MayStop(" + stopProbability + ")"
}

// TODO: DropUnneededItems

trait PickUpItems extends AIState {
  override def act: Action = {
    val unknownItems = character.map.items.filter(noticesItem)
    if (!unknownItems.isEmpty) util.Debug.log("Unknown items: " + unknownItems)
    val unknownItemHere = unknownItems.find(character.location == _.location)
    unknownItemHere foreach { it => util.Debug.log("Found item: " + it.description)}
    unknownItemHere.map(PickUp).orElse(unknownItems.firstOption.flatMap(it => pathFinder.stepTowards(it.location))).getOrElse(super.act)
  }

  def noticesItem(it: Item): Boolean = {
    if (it.location != character.location && character.visibility(it.location) != FullyVisible)
      false
    else
      !ItemKnowledge.contains(it)
  }
}

trait LookOutForPlayer extends AIState with PickUpItems {
  def playerNoticedAction = {
    TimeLine.schedule(50) { // Small delay so they can't get themselves in a loop
      if (character.alife)
        character.emitMessage(Shout(character))
    }
    switchTo(new FollowAndAttackPlayerState)
  }

  def investigate(loc: Point) = switchTo(new InvestigateLocationState(loc) with ReplacingLookOutForPlayer)

  def switchTo(state: AIState) = push(state)

  def foundCorpseAction = {
    character.emitMessage(Shout(character))
    investigate(character.location)
  }

  override def handleMessage(msg: Message) = msg match {
    case AttackMessage(Player, ch, _, _, _, _) if ch == character => Some(playerNoticedAction)
    case BumpMessage(ch, location, true) if ch == character && ch.map.getCharacterAt(location) == Some(Player) => Some(playerNoticedAction)
    case HitByProjectileMessage(ch, _, _, _) =>
      if (ch == character)
        character.emitMessage(Shout(character))
      Some(investigate(Player.location))
    case DeathMessage(ch) if ch != character =>
      character.map.items.filter(_.location == ch.location) foreach ItemKnowledge.add
      super.handleMessage(msg)
    case Shout(ch) if ch != character => Some(investigate(ch.location))
    case Noise(origin) =>
      msg match {
        case _: Shout#Noise => Some(investigate(origin))
        case _ => 
          if (character.visibility(origin) == Visible)
            Some(investigate(origin))
          else
            None
      }
    case UseStairsMessage(ch, location) if ch == Player => Some(switchTo(new UseStairsState(location) with ReplacingLookOutForPlayer))
    case MoveMessage(ch, from, to) if ch == Player && character.visibility(from) != FullyVisible => Some(investigate(to))
    case AttackMessage(ch, target, _, _, _, _) if character.visibility(ch.location) == Visible => Some(investigate(ch.location))
    case OpenDoorMessage(_, door) if character.visibility(door) != FullyVisible => Some(investigate(door))
    case CloseDoorMessage(_, door) if character.visibility(door) != FullyVisible => Some(investigate(door))
    case _ => super.handleMessage(msg)
  }

  override def act: Action = {
    if (character.map == Player.map && 
        (character.location.neighbours(true).contains(Player.location) || character.visibility(Player.location) == FullyVisible) &&
        character.detects(Player.location, Player.hidingValue)) {
      playerNoticedAction
    } else if (character.map.items.exists(it => it.isInstanceOf[Corpse] && it.location == character.location && !ItemKnowledge.contains(it))) {
      character.map.items.filter(it => it.isInstanceOf[Corpse] && it.location == character.location) foreach ItemKnowledge.add
      foundCorpseAction
    } else
      super.act
  }

  override def toString = super.toString + " + LookOutForPlayer"
}

trait ReplacingLookOutForPlayer extends LookOutForPlayer {
  override def switchTo(state: AIState) = replace(state)

  override def toString = super.toString + "/r"
}

case class Shout(character: Character) extends CharacterMessage(character) {
  override def noise = Some(new Noise(character))

  class Noise(character: Character) extends roguelike.Noise(character.location) {
    def loudness = 15
  }
}

class FollowAndAttackPlayerState extends AIState {
  private var lastSeen: Option[Point] = None

  def playerLostAction = lastSeen match {
    case Some(location) if isStairs(location) => replace(new UseStairsState(location) with ReplacingLookOutForPlayer)
    case Some(location) => replace(new InvestigateLocationState(location) with ReplacingLookOutForPlayer)
    case None => replace(new InvestigateLocationState(character.location) with ReplacingLookOutForPlayer)
  }

  def isStairs(loc: Point) = character.map.getTile(loc) match {
    case UpStairs(_) | DownStairs(_) => true
    case _ => false
  }

  override def awareOf(ch: Character) = if (ch == Player) true else super.awareOf(ch)

  override def handleMessage(msg: Message) = msg match {
    case UseStairsMessage(ch, location) if ch == Player => Some(replace(new UseStairsState(location) with ReplacingLookOutForPlayer))
    case _ => super.handleMessage(msg)
  }

  override def act: Action = {
    if (character.ranged.isDefined && character.loadedAmmo.isEmpty && canLoadRangedWeapon) {
      loadRangedWeapon
    }
    else if (character.ranged.isDefined && character.loadedAmmo.isDefined && character.canFireAt(Player)) {
      FireRangedWeapon(Player)
    }
    else if (character.location.neighbours(true) contains Player.location) {
      lastSeen = Some(Player.location)
      AttackCharacter(Player)
    } else if (character.map == Player.map && character.visibility(Player.location) == FullyVisible) {
      lastSeen = Some(Player.location)
      pathFinder.stepTowards(Player.location) getOrElse Wait
    } else {
      util.Debug.log("Lost player at " + Player.location + "; last seen at " + lastSeen + "; current location " + character.location)
      playerLostAction
    }
  }

  def canLoadRangedWeapon = character match {
    case inv: HasInventory => inv.inventory.exists(isFireableProjectile)
    case _ => false
  }

  def loadRangedWeapon = character match {
    case inv: HasInventory => LoadRangedWeapon(character.ranged.get, inv.removeOne(inv.inventory.find(isFireableProjectile).get).asInstanceOf[Projectile])
  }

  def isFireableProjectile(it: Item) = it match {
    case p: Projectile => character.ranged.exists(_.fires(p))
    case ItemStack((p: Projectile) :: _) => character.ranged.exists(_.fires(p))
    case _ => false
  }

  override def toString = "FollowAndAttackPlayer"
}

case class GoToLocationState(location: Point) extends AIState {
  override def act: Action = {
    if (character.location == location)
      pop
    else {
      pathFinder.stepTowards(location) getOrElse Wait
    }
  }

  override def toString = "GoTo " + location
}

case class UseStairsState(location: Point) extends AIState {
  private var finished = false

  lazy val map = character.map

  override def act: Action = {
    if (character.map != map)
      pop
    else if (character.location == location) {
      character.currentTile match {
        case UpStairs(_) => GoUpStairs
        case DownStairs(_) => GoDownStairs
        case _ => util.Debug.log("There's no stairs where there should be!"); pop
      }
    } else {
      pathFinder.stepTowards(location) getOrElse Wait
    }
  }

  override def toString = "UseStairs " + location
}

case class InvestigateLocationState(location: Point) extends AIState {
  // TODO: Would be better if this would be replaced when the player is noticed
  var checkedLocations: Set[Point] = Set.empty
  var investigationPath: List[Point] = Nil
  
  def interesting(p: Point) = {
    !checkedLocations.contains(p) && !character.map.getTile(p).blocking && {
      character.map.getCharacterAt(p) match {
        case Some(n: NPC) => false
        case _ => true
      }
    }
  }

  override def act: Action = {
    checkedLocations += character.location
    if (checkedLocations contains location) {
      Random.maybe(10, pop) or {
        val uncheckedNeighbours = character.location.neighbours(true).toList.filter(interesting)
        if (uncheckedNeighbours.isEmpty) {
          val back = investigationPath.firstOption getOrElse location
          investigationPath = if (investigationPath.isEmpty) Nil else investigationPath.tail
          pathFinder.stepTowards(back) getOrElse Wait
        } else {
          investigationPath ::= character.location
          Move(Random.chooseOne(uncheckedNeighbours) - character.location)
        }
      }
    } else {
      pathFinder.stepTowards(location) getOrElse {
        checkedLocations += location // Don't try to get there anymore
        Wait
      }
    }
  }

  override def toString = "Investigate " + location
}
