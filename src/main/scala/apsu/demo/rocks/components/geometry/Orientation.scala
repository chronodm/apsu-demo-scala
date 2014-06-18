package apsu.demo.rocks.components.geometry

import org.apache.commons.math3.util.MathUtils

/**
 * Orientation, as an angle in radians (normalized to +/- 2pi)
 *
 * @author david
 */
class Orientation private(val theta: Double) {
    def +(dTheta: Double): Orientation = {
      Orientation.apply(theta + dTheta)
    }

    def -(dTheta: Double): Orientation = {
      Orientation.apply(theta - dTheta)
    }


  def canEqual(other: Any): Boolean = other.isInstanceOf[Orientation]

  override def toString: String = s"Orientation($theta)"

  override def equals(other: Any): Boolean = other match {
    case that: Orientation =>
      (that canEqual this) &&
        theta == that.theta
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(theta)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object Orientation {
  def apply(t: Double): Orientation = {
    val theta = MathUtils.normalizeAngle(t, 0)
    new Orientation(theta)
  }

  def unapply(a: Orientation): Option[Double] = {
    Some(a.theta)
  }
}
