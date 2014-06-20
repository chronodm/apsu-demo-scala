package apsu.demo.rocks.components.geometry

/**
 * A position in Java2d user space
 *
 * @author david
 */
case class Position(x: Double, y: Double) {
  override def toString: String = s"($x, $y)"

  def distanceTo(o: Position) = {
    Math.sqrt(Math.pow(x - o.x, 2) + Math.pow(y - o.y, 2))
  }
}
