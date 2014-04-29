package roguelike.characters

import roguelike.util.Random
import roguelike.items._
import roguelike.characters.ai._
import roguelike.map.geometry.Point

class Goblin extends Humanoid with NPC {
  lazy val controller: StateAI = new StateAI(this, initialState)
  def initialState: AIState = new WaitState with LookOutForPlayer
  
  val maxHP = Random.next(1, 3)
  val strength = 5 + (Random.next(-1, 2) + Random.next(-1, 2) + Random.next(-1, 2)) / 2
  val agility = 7 + (Random.next(-1, 2) + Random.next(-1, 2) + Random.next(-1, 2)) / 2

  def detect = 12

  def description = "the goblin"

  override def corpse = Some(new GoblinCorpse)
}

class GoblinCorpse extends Corpse {
  override def description = "goblin " + super.description
}

class GoblinGuard extends Goblin {
  addAndEquip(new Torch)
  addAndEquip(new Dagger)

  override def initialState = {
    val route = generatePatrolRoute
    util.Debug.log("Patrol route: " + route)
    new PatrolState(route) with LookOutForPlayer
  }

  def generatePatrolRoute: List[Point] = {
    val len = Random.maybe(40, 2) or (2 + Random.next(4))
    generatePatrolRoute(len)
  }

  def generatePatrolRoute(len: Int): List[Point] = {
    if (len == 1)
      location :: Nil
    else {
      val routeSoFar = generatePatrolRoute(len - 1)
      val next = routeSoFar.head
      val mapView = new roguelike.map.TranslatedMapView(map.visibilityView)
      mapView.setX(next.x); mapView.setY(next.y)
      val fov = new roguelike.map.fov.DiamondFieldOfView(map.visibilityView, 20)
      fov.update
      (for {
        counter <- Stream.range(0, 500)
        val candidate = Point(Random.next(-20, 20), Random.next(-20, 20))
        if candidate.rogueLength > 3 && fov.isVisible(candidate.x, candidate.y)
      } yield (candidate + next)).firstOption.map(_ :: routeSoFar).getOrElse(routeSoFar)
    }
  }

  override def description = super.description + " guard"
}

class GoblinWarrior extends Goblin {
  addAndEquip(new LeatherArmour)
  addAndEquip(Random.chooseOne(List(() => new Sword, () => new Axe))())

  override def description = super.description + " warrior"
}

class GoblinArcher extends Goblin {
  addAndEquip(new Bow)
  addToInventory(new ItemStack(Random.next(3, 6), () => new Arrow))

  override def aimingValue = super.aimingValue + 6

  override def description = super.description + " archer"
}

class GoblinElite extends Goblin {
  addAndEquip(new LeatherArmour)
  if (Random.coin) {
    addAndEquip(new Axe)
    addAndEquip(new Shield)
  } else {
    addAndEquip(new Warhammer)
  }

  override val maxHP = 2
  override val strength = 9
  override val agility = 9

  override def detect = 9

  override def description = super.description + " elite"
}

object GoblinKing extends Goblin {
  addAndEquip(new LeatherArmour)
  addAndEquip(new Sword)

  override val maxHP = 2
  override val strength = 7
  override val agility = 8

  override def detect = 9

  override def description = super.description + " king"
}

trait GoblinKingsGuard extends Goblin {
  override lazy val controller = {
    new StateAI(this, new GuardCharacterState(location, GoblinKing))
  }
}
