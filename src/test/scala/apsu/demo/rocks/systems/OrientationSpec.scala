package apsu.demo.rocks.systems

import org.scalatest.{FlatSpec, Matchers}
import apsu.demo.testutils.FloatEquality
import apsu.demo.rocks.components.geometry.Orientation

/**
 * OrientationSpec
 *
 * @author david
 */
class OrientationSpec extends FlatSpec with Matchers {

  implicit val doubleEq = new FloatEquality(3)

  val pi = Math.PI.asInstanceOf[Float]
  val piOver3 = pi / 3

  "+/-" should "add/subtract an angle" in {
    val o0 = Orientation(piOver3)
    val o1 = o0 + piOver3
    o1.theta should be(2 * piOver3)
    val o2 = o1 - piOver3
    o2.theta should be(o0.theta)
  }

  it should "normalize angles to +/- pi" in {
    val o0 = Orientation(piOver3)
    val o1 = o0 + pi
    o1.theta should ===(-2 * piOver3)
  }

  it should "normalize negative angles" in {
    val o0 = Orientation(-pi / 3)
    val o1 = o0 - pi
    o1.theta should ===(2 * piOver3)
  }

  "apply()" should "produce something that works with pattern matching" in {
    val o = Orientation(1)
    val matched = o match {
      case Orientation(1) => true
    }
  }

  "equals" should "return true for identical objects" in {
    val o0 = Orientation(piOver3)
    (o0 equals o0) should be(true)
  }

  it should "return true for equivalent objects" in {
    val o0 = Orientation(piOver3)
    val o1 = Orientation(piOver3)
    (o0 equals o1) should be(true)
  }

  it should "return true for equivalent objects with denormalized angles" in {
    // Note: This happens to work for pi/4 but for many other
    // values the normalized angle will be off in like the 15th
    // decimal place.

    // TODO: consider snapping to some fraction of a circle cf. http://docs.oracle.com/javase/7/docs/api/java/awt/geom/AffineTransform.html

    val o0 = Orientation(pi / 4)
    val o1 = Orientation(2 * pi + pi / 4)
    o0.theta should be(o1.theta)
    (o0 equals o1) should be(true)
  }

  "==" should "return true for identical objects" in {
    val o0 = Orientation(piOver3)
    (o0 == o0) should be(true)
  }

  it should "return true for equivalent objects" in {
    val o0 = Orientation(piOver3)
    val o1 = Orientation(piOver3)
    (o0 == o1) should be(true)
  }

  it should "return true for equivalent objects with denormalized angles" in {
    // Note: This happens to work for pi/4 but for many other
    // values the normalized angle will be off in like the 15th
    // decimal place.

    // TODO: consider snapping to some fraction of a circle cf. http://docs.oracle.com/javase/7/docs/api/java/awt/geom/AffineTransform.html

    val o0 = Orientation(pi / 4)
    val o1 = Orientation(2 * pi + pi / 4)
    o0.theta should be(o1.theta)
    (o0 == o1) should be(true)
  }

}
