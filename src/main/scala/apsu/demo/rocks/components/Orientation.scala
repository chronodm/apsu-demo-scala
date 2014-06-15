package apsu.demo.rocks.components

import org.apache.commons.math3.util.MathUtils

/**
 * Orientation, as an angle in radians
 *
 * @author david
 */
case class Orientation(theta: Double) {

  def +(dTheta: Double): Orientation = {
    Orientation(MathUtils.normalizeAngle(theta + dTheta, 0))
  }

  def -(dTheta: Double): Orientation = {
    this + (-dTheta)
  }

}
