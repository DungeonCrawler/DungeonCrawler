package roguelike.characters

import roguelike.map._
import roguelike.map.geometry.Point
import roguelike.items.Torch

abstract class Action { // TODO: Pass Character as constructor parameter
  // TODO LongAction, for loading of crossbows
  def duration: Long

  def valid(character: Character): Boolean

  def execute(character: Character)
}

case object Wait extends Action {
  def duration = 1000
  def valid(character: Character) = true
  def execute(character: Character) = { }
}

case class Move(move: Point) extends Action {
  def duration = 1000

  def valid(character: Character) = {
    if (Math.abs(move.x) > 1 && Math.abs(move.y) > 1)
      false
    else if ((character.map.getTile(character.location + move).blocking || character.map.getCharacterAt(character.location + move).isDefined)
             && character.visibility(character.location + move).visible)
      false
    else
      true
  }

  def execute(character: Character) = {
    if (character.map.getTile(character.location + move).blocking)
      character.emitMessage(BumpMessage(character, character.location + move, false))
    else if (character.map.getCharacterAt(character.location + move).isDefined)
      character.emitMessage(BumpMessage(character, character.location + move, true))
    else {
      character.lastMove = TimeLine.time
      character.emitMessage(MoveMessage(character, character.location, character.location + move))
      character.location += move
    }
  }
}

case class BumpMessage(character: Character, location: Point, otherCharacter: Boolean) extends CharacterMessage(character)

case class MoveMessage(character: Character, fromLocation: Point, toLocation: Point) extends CharacterMessage(character) {
  override lazy val noise = {
    val loudness = stepNoiseLoudness
    if (loudness > 0)
      Some(new StepNoise(fromLocation, loudness))
    else
      None
  }

  def stepNoiseLoudness: Int = {
    val sneakingValue = character.sneakingValue
    val max = 7
    (0 to max).dropWhile(_ => util.Random.nd0m(6, 6) > sneakingValue).firstOption.getOrElse(max)
  }
}

class StepNoise(location: Point, val loudness: Int) extends Noise(location)

case class OpenDoor(move: Point) extends Action {
  def duration = 1000

  def valid(character: Character) = {
    if (Math.abs(move.x) > 1 && Math.abs(move.y) > 1)
      false
    else character.map.getTile(character.location + move) match {
      case Door(false) => true
      case _ => false
    }
  }

  def execute(character: Character) = {
    val door = character.map.getTile(character.location + move).asInstanceOf[Door]
    door.open = true
    character.map.bumpVersion
    character.emitMessage(OpenDoorMessage(character, character.location + move))
  }
}

case class CloseDoor(move: Point) extends Action {
  def duration = 1000

  def valid(character: Character) = {
    if (Math.abs(move.x) > 1 && Math.abs(move.y) > 1)
      false
    else character.map.getTile(character.location + move) match {
      case Door(true) => true
      case _ => false
    }
  }

  def execute(character: Character) = {
    val door = character.map.getTile(character.location + move).asInstanceOf[Door]
    door.open = false
    character.map.bumpVersion
    character.emitMessage(CloseDoorMessage(character, character.location + move))
  }
}

case class OpenDoorMessage(character: Character, doorLocation: Point) extends CharacterMessage(character)
case class CloseDoorMessage(character: Character, doorLocation: Point) extends CharacterMessage(character)

case object GoDownStairs extends Action {
  def duration = 1000

  def valid(character: Character) = character.currentTile match {
    case DownStairs(newMap) => true
    case _ => false
  }

  def execute(character: Character) = character.currentTile match {
    case DownStairs(newMap) =>
      if (newMap().getCharacterAt(character.location).isEmpty) {
        character emitMessage UseStairsMessage(character, character.location)
        character.changeMap(newMap())
        character emitMessage ComeFromStairsMessage(character)
      } else {
        character emitMessage StairsBlockedMessage(character)
      }
  }
}

case object GoUpStairs extends Action {
  def duration = 1000

  def valid(character: Character) = character.currentTile match {
    case UpStairs(newMap) => true
    case DungeonExit if character == Player => true
    case _ => false
  }

  def execute(character: Character) = character.currentTile match {
    case UpStairs(newMap) =>
      if (newMap().getCharacterAt(character.location).isEmpty) {
        character emitMessage UseStairsMessage(character, character.location)
        character.changeMap(newMap())
        character emitMessage ComeFromStairsMessage(character)
      } else {
        character emitMessage StairsBlockedMessage(character)
      }
    case DungeonExit if character == Player => Player.controller.endGame
  }
}

case class UseStairsMessage(character: Character, location: Point) extends CharacterMessage(character)
case class ComeFromStairsMessage(character: Character) extends CharacterMessage(character)
case class StairsBlockedMessage(character: Character) extends CharacterMessage(character)

case object TakeTorch extends Action {
  def duration = 1000

  def valid(character: Character) = character.currentTile match {
    case t: TorchHolder if t.hasTorch => character.isInstanceOf[HasInventory]
    case _ => false
  }

  def execute(character: Character) = character.currentTile match {
    case t: TorchHolder => 
      character emitMessage TakeTorchMessage(character)
      character.asInstanceOf[HasInventory] addToInventory t.get(character.map)
  }
}

case class PutTorch(torch: Torch) extends Action {
  def duration = 1000

  def valid(character: Character) = character.currentTile match {
    case t: TorchHolder if !t.hasTorch => true
    case _ => false
  }

  def execute(character: Character) = character.currentTile match {
    case t: TorchHolder => 
      character emitMessage PutTorchMessage(character)
      t.put(torch, character.map, character.location)
  }
}

case class TakeTorchMessage(character: Character) extends CharacterMessage(character)
case class PutTorchMessage(character: Character) extends CharacterMessage(character)
