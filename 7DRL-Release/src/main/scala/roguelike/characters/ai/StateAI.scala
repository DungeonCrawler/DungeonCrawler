package roguelike.characters.ai

import scala.collection.immutable.Queue

class StateAI(val character: NPC, initialState: AIState) extends NPCController {
  initialState.ai = this
  var states: List[AIState] = List(initialState)
  var plannedAction: Option[Action] = None

  override def handleMessage(msg: Message) = {
    assert(!states.isEmpty)
    states foreach (_.ai = this)
    plannedAction = states.head.handleMessage(msg)
  }

  def act = sanitize {
    assert(!states.isEmpty)
    states foreach (_.ai = this)
    val action = plannedAction.getOrElse(states.head.act)
    plannedAction = None
    action
  }

  def sanitize(action: Action) = {
    if (!action.valid(character)) {
      util.Debug.log("Invalid action: " + action + " probably returned by " + states.head)
      Wait
    } else action
  }

  override def attentive = states.head.attentive
  override def awareOf(character: Character) = states.head.awareOf(character)

  lazy val pathFinder = new MonsterPathFinder(character)
}

trait AIState {
  private[ai] var ai: StateAI = null

  def character = ai.character

  def handleMessage(msg: Message): Option[Action] = {
    None
  }

  def attentive = true

  def awareOf(character: Character) = if (character == Player) false else true

  def pathFinder = ai.pathFinder

  def act: Action = {
    Wait
  }

  def push(state: AIState): Action = {
    //assert(this eq ai.states.head)
    util.Debug.log("Pushed state: " + state)
    if (this ne ai.states.head) util.Debug.log("Wrong state on top! this: " + this + "; stack: " + ai.states.mkString(", "))
    state.ai = ai
    ai.states ::= state
    state.act
  }

  def replace(state: AIState): Action = {
    //assert(this eq ai.states.head)
    util.Debug.log("Replaced " + this + " by " + state)
    if (this ne ai.states.head) println("Wrong state on top! this: " + this + "; stack: " + ai.states.mkString(", "))
    state.ai = ai
    ai.states = state :: ai.states.tail
    state.act
  }

  def pop: Action = {
    //assert(this eq ai.states.head)
    if (this ne ai.states.head) util.Debug.log("Wrong state on top! this: " + this + "; stack: " + ai.states.mkString(", "))
    ai.states = ai.states.tail
    util.Debug.log("Popped " + this + ", new top: " + ai.states.head)
    ai.states.head.act
  }
}

abstract class StateSequence extends AIState {
  var currentStates: List[AIState] = states

  def states: List[AIState]

  override def act: Action = {
    if (currentStates == Nil)
      pop
    else if (currentStates.size == 1)
      replace(currentStates.head)
    else {
      val newState = currentStates.head
      currentStates = currentStates.tail
      push(newState)
    }
  }

  override def toString = currentStates.mkString("(", ", ", ")")
}
