package apsu.demo.rocks.components

/**
 * A position in Java2d user space
 *
 * @author david
 */
case class Position(x: Double, y: Double) {
  override def toString: String = s"($x, $y)"
}
