package apsu.demo.rocks.components

import org.apache.commons.math3.util.MathUtils

/**
 * Orientation, as an angle in radians
 *
 * @author david
 */
sealed trait Orientation {
  def theta: Double

  def +(dTheta: Double): Orientation = {
    Orientation(theta + dTheta)
  }

  def -(dTheta: Double): Orientation = {
    Orientation(theta - dTheta)
  }
}

object Orientation {
  def apply(t: Double): Orientation = {
    val theta = MathUtils.normalizeAngle(t, 0)
    OrientationImpl(theta)
  }

  private case class OrientationImpl(theta: Double) extends Orientation

}
