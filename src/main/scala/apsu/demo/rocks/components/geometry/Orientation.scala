package apsu.demo.rocks.components.geometry

import org.apache.commons.math3.util.MathUtils

/**
 * Orientation, as an angle in radians (normalized to +/- 2pi)
 *
 * @author david
 */
class Orientation private(val theta: Float) {
    def +(dTheta: Float): Orientation = {
      Orientation.apply(theta + dTheta)
    }

    def -(dTheta: Float): Orientation = {
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
  def apply(t: Float): Orientation = {
    val theta = MathUtils.normalizeAngle(t, 0)
    Orientation(theta)
  }

  def apply(t: Double): Orientation = {
    new Orientation(t.asInstanceOf[Float])
  }

  def unapply(a: Orientation): Option[Float] = {
    Some(a.theta)
  }
}
