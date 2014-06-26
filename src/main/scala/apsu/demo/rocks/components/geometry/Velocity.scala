package apsu.demo.rocks.components.geometry

/**
 * Velocity, in Java2D userspace pixels per second,
 * represented as a vector
 *
 * @author david
 */
case class Velocity(x: Float, y: Float) {

  def +(that: Velocity): Velocity = {
    Velocity(x + that.x, y + that.y)
  }

  def -(that: Velocity): Velocity = {
    Velocity(x - that.x, y - that.y)
  }

  def +(that: Float): Velocity = {
    Velocity.fromPolar(magnitude + that, theta)
  }

  def -(that: Float): Velocity = {
    Velocity.fromPolar(magnitude - that, theta)
  }

  lazy val magnitude: Float = {
    Math.sqrt(x*x + y*y).asInstanceOf[Float]
  }

  lazy val theta: Float = {
    Math.atan2(y, x).asInstanceOf[Float]
  }

  override def toString: String = s"<$x, $y>"
}

object Velocity {

  def apply(x: Double, y: Double): Velocity = Velocity(x.asInstanceOf[Float], y.asInstanceOf[Float])

  def fromCartesian(x: Float, y: Float): Velocity = Velocity(x, y)

  def fromPolar(magnitude: Float, theta: Float): Velocity = {
    val vX = Math.cos(theta) * magnitude
    val vY = Math.sin(theta) * magnitude
    Velocity(vX, vY)
  }
}
