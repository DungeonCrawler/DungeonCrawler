package roguelike.combat

import roguelike.characters._
import roguelike.util.Random
import roguelike.map.geometry.{Line, Point}

trait Combatant {
  this: Character =>
  
  def maxHP: Int
  def strength: Int
  def agility: Int
  
  private var _hp: Int = 1
  private var _alife = true

  def hp = _hp
  def doDamage(amount: Int) = {
    if (alife) {
      _hp -= amount
      if (_hp <= 0)
        die
    }
  }
  def heal(amount: Int) = {
    if (alife) {
      _hp = Math.min(maxHP, _hp + amount)
    }
  }
  
  def alife = _alife
  def die = {
    if (alife) {
      for (c <- corpse) {
        c.location = location
        c.changeMap(Some(map))
      }
      emitMessage(DeathMessage(this))
      _alife = false
      changeMap(null)
    }
  }

  def corpse: Option[items.Corpse] = None
  
  def comeToLife = {
    _hp = maxHP
    _alife = true
  }

  def awareOf(character: Character) = true
  def stabResistance: Int
  def stabbingModifier: Int = 0
  
  def attack: Option[Attack]
  def parry: Option[Parry]
  def ranged: Option[Ranged]
  def armours: List[Armour]
  
  def performAttack(target: Character) = {
    assert(target.alife)
    val at = Math.min(strength, attack.get.maxStrength) + Math.min(agility, attack.get.maxAgility)
    val targetIsAware = target.awareOf(this)
    util.Debug.log("Attacking with " + at + (if (targetIsAware) "" else " (target is unaware of the attacker!)"))
    if (!(targetIsAware && target.performParry(at, false))) {
      // TODO: Evasion

      var stabbed = false
      val agi = Math.min(agility, attack.get.maxAgility) + stabbingModifier
      def stabChance: Boolean = {
        if (targetIsAware)
          false
        else {
          val d = Random.nd0m(agi, 4)
          util.Debug.log("Stab check: agi=" + agi + " => " + d + "/" + target.stabResistance)
          if (d > target.stabResistance) {
            stabbed = true
            true
          } else
            false
        }
      }

      val effects = (attack.get.effects takeWhile (Random.next(100) < _._1 || stabChance)) map (_._2)
      
      util.Debug.log("Hit! (x" + effects.size + ")")
      
      val armour = Math.min(target.applyArmour, effects.size)
      val stabArmourBypass = ((1 to armour) takeWhile (_ => stabChance)).size

      if (armour > 0) util.Debug.log("Armour prevents " + armour + (if (stabArmourBypass > 0) ", but stabbing bypasses " + stabArmourBypass else ""))

      val armourApplied = effects take (effects.size - armour + stabArmourBypass)

      this.emitMessage(AttackMessage(this, target, false, false, armourApplied.size == 0, stabbed))
      
      for (e <- armourApplied)
        e(target)
    }
    else {
      util.Debug.log("Parried!")
      this.emitMessage(AttackMessage(this, target, true, false, false, false))
    }
  }
  
  def applyArmour: Int = {
    val armourValues = armours map (armour => (armour.prevention takeWhile (Random.next(100) < _)).size)
    armourValues.foldLeft(0)((a, b) => Math.max(a, b))
  }
  
  var canParry = true
  
  def performParry(at: Int, projectile: Boolean): Boolean = {
    if (!canParry || !parry.isDefined || (projectile && parry.forall(!_.canBlockProjectiles)))
      false
    else {
      canParry = false
      TimeLine.schedule(1000) {
        canParry = true
      }
      val pa = Math.min(agility, parry.get.maxAgility) * 2
      val d = Random.nd0m(pa, 4)
      util.Debug.log("Parrying with " + pa + ", " + d + " vs. " + at + (if (projectile) " (projectile)" else ""))
      d > at
    }
  }

  def aimingValue: Int = 8
  var loadedAmmo: Option[Projectile] = None
  var lastAiming: (Long, Character) = (-2000, null)

  def fireAt(at: Character) = {
    assert(at.alife)
    val aimed = lastAiming._2 == at && lastAiming._1 + 1100 >= TimeLine.time
    val distModifier = (at.location rogueDist location) / 3
    val aiming = aimingValue + (if (aimed) 6 else 0) + (if (visibility(at.location) == FullyVisible) 0 else -3) - distModifier
    val ammo = loadedAmmo.get
    val targetIsAware = at.awareOf(this)
    loadedAmmo = None
    util.Debug.log("Firing at " + at.description + " with aiming " + aiming)
    if (Random.nd02(aiming) > 10) {
      util.Debug.log("Hit!")
      if (!targetIsAware || !at.performParry(16, true)) {
        val aimingDifficulty = 17
        def aimChance: Boolean = {
          val d = Random.nd0m(aiming, 4)
          util.Debug.log("Aiming check: aiming=" + aiming + " => " + d + "/" + aimingDifficulty)
          if (d > aimingDifficulty) {
            true
          } else
            false
        }

        val effects = (ammo.effects takeWhile (Random.next(100) < _._1 || aimChance)) map (_._2)

        util.Debug.log("Hit! (x" + effects.size + ")")

        val armour = Math.min(at.applyArmour, effects.size)
        val aimArmourBypass = ((1 to armour) takeWhile (_ => aimChance)).size

        if (armour > 0) util.Debug.log("Armour prevents " + armour + (if (aimArmourBypass > 0) ", but aiming bypasses " + aimArmourBypass else ""))

        val armourApplied = effects take (effects.size - armour + aimArmourBypass)

        at.emitMessage(HitByProjectileMessage(at, ammo, false, armourApplied.size == 0))

        for (e <- armourApplied)
          e(at)
      } else {
        at.emitMessage(HitByProjectileMessage(at, ammo, true, false))
      }
    }
  }

  def canFireAt(at: Character) = {
    at != this && Line(location, at.location).points.forall(noProjectileBlock(at)) && visibility(at.location).visible
  }

  private def noProjectileBlock(at: Character)(loc: Point) = {
    !map.getTile(loc).blocking && (map.getCharacterAt(loc) match {
      case Some(ch) if ch == this || ch == at => true
      case None => true
      case _ => false
    })
  }
}

trait Attack {
  def maxStrength: Int
  def maxAgility: Int
  def effects: List[(Int, Character => Unit)]
  
  def damage(probabilities: Int*) = (100, (ch: Character) => ()) :: (probabilities.toList zip List.make(probabilities.size, (ch: Character) => ch.doDamage(1)))
}

trait Parry {
  def maxAgility: Int

  def description: String

  def canBlockProjectiles = false
}

trait Armour {
  // TODO: maxAgility

  def prevention: List[Int]
  
  def preventDamage(probabilities: Int*) = probabilities.toList

  def description: String
}

trait Ranged {
  def fires(ammo: Projectile): Boolean

  def loadingTime: Long

  def description: String
}

trait Projectile {
  def effects: List[(Int, Character => Unit)]

  def damage(probabilities: Int*) = (100, (ch: Character) => ()) :: (probabilities.toList zip List.make(probabilities.size, (ch: Character) => ch.doDamage(1)))

  def description: String
}

case class AttackCharacter(target: Character) extends Action {
  def duration = 900
  
  def valid(character: Character) = {
    character.attack.isDefined && character.map == target.map && (character.location.neighbours(true) contains target.location)
  }
  
  def execute(character: Character) = {
    character.performAttack(target)
  }
}

case class AttackMessage(attacker: Character, attacked: Character, 
                         parried: Boolean, evaded: Boolean, hitArmour: Boolean, stab: Boolean) extends CharacterMessage(attacker) {
  // TODO: use a single enum instead of three booleans
  override def noise = {
    if (parried || hitArmour)
      Some(new ClangingNoise(attacked.location))
    else
      super.noise
  }

  override def involved = attacked :: super.involved
}

case class LoadRangedWeapon(weapon: Ranged, projectile: Projectile) extends Action {
  def duration = weapon.loadingTime

  def valid(character: Character) = {
    character.ranged == Some(weapon) && weapon.fires(projectile) && character.loadedAmmo == None
  }

  def execute(character: Character) = {
    character.loadedAmmo = Some(projectile)
    character.emitMessage(LoadRangedWeaponMessage(character))
  }
}

case class LoadRangedWeaponMessage(character: Character) extends CharacterMessage(character)

case class AimRangedWeapon(at: Character) extends Action {
  def duration = 1000

  def valid(character: Character) = {
    character.ranged.isDefined && character.canFireAt(at)
  }

  def execute(character: Character) = {
    character.lastAiming = (TimeLine.time, at)
    character.emitMessage(AimRangedWeaponMessage(character, at))
  }
}

case class AimRangedWeaponMessage(character: Character, at: Character) extends CharacterMessage(character)

case class FireRangedWeapon(at: Character) extends Action {
  def duration = 200

  def valid(character: Character) = {
    character.ranged.isDefined && character.loadedAmmo.isDefined && character.canFireAt(at)
  }

  def execute(character: Character) = {
    character.emitMessage(FireRangedWeaponMessage(character, at))
    character.fireAt(at)
  }
}

case class FireRangedWeaponMessage(character: Character, at: Character) extends CharacterMessage(character) {
  // TODO: Noise
}

case class HitByProjectileMessage(character: Character, projectile: Projectile, blocked: Boolean, hitArmour: Boolean) extends CharacterMessage(character)

case class DeathMessage(character: Character) extends CharacterMessage(character)
