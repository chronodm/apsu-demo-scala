package apsu.demo.testutils

import org.scalactic.Equality
import java.math.MathContext
import apsu.demo.rocks.components.geometry.{Orientation, Position}

/**
 * Checks double equality to the specified number of
 * significant digits.
 * @param significantDigits The number of significant digits.
 */
class FloatEquality(significantDigits: Int) extends Equality[Float] {
  private val mc = new MathContext(significantDigits)

  override def areEqual(f1: Float, b: Any): Boolean = {
    b match {
      case f2: Float =>
        val bd1 = BigDecimal(f1.toDouble).round(mc)
        val bd2 = BigDecimal(f2.toDouble).round(mc)
        bd1 == bd2
      case _ => false
    }
  }
}

/**
 * Checks position equality in x and y to three significant digits.
 */
class PositionEquality extends Equality[Position] {
  private val de = new FloatEquality(3)

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
  private val de = new FloatEquality(3)

  override def areEqual(o: Orientation, a: Any): Boolean = {
    a match {
      case o2: Orientation =>
         de.areEqual(o.theta, o2.theta)
      case _ => false
    }
  }
}
