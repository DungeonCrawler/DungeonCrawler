package roguelike.characters

trait NPC extends Character {
  override def controller: NPCController

  override def attentive = controller.attentive

  override def awareOf(character: Character) = controller.awareOf(character)
}

trait NPCController extends Controller {
  def attentive = true
  def awareOf(character: Character) = true
}