package apsu.demo.rocks.components.geometry

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

  def +(that: Double): Velocity = {
    Velocity.fromPolar(magnitude + that, theta)
  }

  def -(that: Double): Velocity = {
    Velocity.fromPolar(magnitude - that, theta)
  }

  lazy val magnitude: Double = {
    Math.sqrt(x*x + y*y)
  }

  lazy val theta: Double = {
    Math.atan2(y, x)
  }

  override def toString: String = s"<$x, $y>"
}

object Velocity {

  def fromCartesian(x: Double, y: Double): Velocity = Velocity(x, y)

  def fromPolar(magnitude: Double, theta: Double): Velocity = {
    val vX = Math.cos(theta) * magnitude
    val vY = Math.sin(theta) * magnitude
    Velocity(vX, vY)
  }
}
