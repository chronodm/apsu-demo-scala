package apsu.demo.testutils

import org.scalautils.Equality
import java.math.MathContext
import apsu.demo.rocks.components.geometry.{Orientation, Position}

/**
 * Checks double equality to the specified number of
 * significant digits.
 * @param significantDigits The number of significant digits.
 */
class DoubleEquality(significantDigits: Int) extends Equality[Double] {
  private val mc = new MathContext(significantDigits)

  override def areEqual(d1: Double, b: Any): Boolean = {
    b match {
      case d2: Double =>
        val bd1 = BigDecimal(d1).round(mc)
        val bd2 = BigDecimal(d2).round(mc)
        bd1 == bd2
      case _ => false
    }
  }
}

/**
 * Checks position equality in x and y to three significant digits.
 */
class PositionEquality extends Equality[Position] {
  private val de = new DoubleEquality(3)

  override def areEqual(p1: Position, a: Any): Boolean = {
    a match {
      case p2: Position =>
        de.areEqual(p1.x, p2.x) && de.areEqual(p1.y, p2.y)
      case _ => false
    }
  }
}

/**
 * Checks orientation angle equality to three significant digits.
 */
class OrientationEquality extends Equality[Orientation] {
  private val de = new DoubleEquality(3)

  override def areEqual(o: Orientation, a: Any): Boolean = {
    a match {
      case o2: Orientation =>
         de.areEqual(o.theta, o2.theta)
      case _ => false
    }
  }
}
