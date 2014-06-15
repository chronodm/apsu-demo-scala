package apsu.demo.rocks.components

/**
 * Velocity, in Java2D userspace pixels per second,
 * represented as a vector
 *
 * @author david
 */
case class Velocity(x: Double, y: Double) {

  def +(that: Velocity): Velocity = {
    Velocity(x + that.x, y + that.y)
  }

  def -(that: Velocity): Velocity = {
    Velocity(x - that.x, y - that.y)
  }

  def length: Double = {
    Math.sqrt(x*x + y*y)
  }

  override def toString: String = s"<$x, $y>"
}
