package roguelike

abstract class Message {
  def noise: Option[Noise] = None

  def detectionDifficulty = 0

  def involved: List[characters.Character] = Nil
}

abstract case class Noise(origin: map.geometry.Point) extends Message {
  def loudness: Int
}

class ClangingNoise(origin: map.geometry.Point) extends Noise(origin) {
  def loudness = 10
}