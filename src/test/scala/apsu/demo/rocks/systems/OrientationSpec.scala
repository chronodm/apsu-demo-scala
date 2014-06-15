package apsu.demo.rocks.systems

import org.scalatest.{FlatSpec, Matchers}
import apsu.demo.rocks.components.Orientation
import apsu.demo.testutils.DoubleEquality

/**
 * OrientationSpec
 *
 * @author david
 */
class OrientationSpec extends FlatSpec with Matchers {

  implicit val doubleEq = new DoubleEquality(3)


  "+/-" should "add/subtract an angle" in {
    val o0 = Orientation(Math.PI / 3)
    val o1 = o0 + (Math.PI / 3)
    o1.theta should be(2 * (Math.PI / 3))
    val o2 = o1 - (Math.PI / 3)
    o2.theta should be(o0.theta)
  }

  it should "normalize angles to +/- pi" in {
    val o0 = Orientation(Math.PI / 3)
    val o1 = o0 + Math.PI
    o1.theta should === (-2 * (Math.PI / 3))
  }

  it should "normalize negative angles" in {
    val o0 = Orientation(-Math.PI / 3)
    val o1 = o0 - Math.PI
    o1.theta should === (2 * (Math.PI / 3))
  }
}
