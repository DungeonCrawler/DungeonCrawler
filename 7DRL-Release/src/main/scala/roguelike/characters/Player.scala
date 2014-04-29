package roguelike.characters

object Player extends Humanoid {
  val controller: ConsoleController = new ConsoleController(this)
  
  var maxHP = 3
  var strength = 7
  var agility = 9

  var hiding = 3
  override def hidingValue = super.hidingValue + hiding
  var sneaking = 3
  override def sneakingValue = super.sneakingValue + sneaking
  var stabbing = 3
  override def stabbingModifier = stabbing
  var aiming = 3
  override def aimingValue = super.aimingValue + aiming

  def detect = 20

  def description = "you"

  var wantsRestart = false

  def reset = {
    inventory = Nil
    maxHP = 3
    strength = 7
    agility = 9
    hiding = 3
    sneaking = 3
    stabbing = 3
    aiming = 3
  }
}
