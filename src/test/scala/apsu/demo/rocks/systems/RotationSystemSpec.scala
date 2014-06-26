package apsu.demo.rocks.systems

import org.scalatest.{Matchers, fixture}
import apsu.core.{MapEntityManager, EntityManager}
import apsu.demo.testutils.OrientationEquality
import java.util.concurrent.TimeUnit
import apsu.demo.rocks.components.geometry.{Orientation, AngularVelocity}

/**
 * RotationSystemSpec
 *
 * @author david
 */
class RotationSystemSpec extends fixture.FlatSpec with Matchers {

  val pi = Math.PI.asInstanceOf[Float]

  case class F(mgr: EntityManager, sys: RotationSystem)

  implicit val orientationEq = new OrientationEquality

  type FixtureParam = F

  def withFixture(test: OneArgTest) = {
    val mgr = new MapEntityManager
    val sys = new RotationSystem(mgr)

    val f = F(mgr, sys)

    withFixture(test.toNoArgTest(f))
  }

  "processTick" should "adjust orientation" in { f =>
    val e = f.mgr.newEntity()
    val o = Orientation(pi/2)
    val w = AngularVelocity(pi/4)
    f.mgr.set(e, o)
    f.mgr.set(e, w)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(1))

    f.mgr.get[Orientation](e).get should === (Orientation(3 * pi / 4))
  }

  it should "take time delta into account" in { f =>
    val e = f.mgr.newEntity()
    val o = Orientation(0)
    val w = AngularVelocity(-pi/4)
    f.mgr.set(e, o)
    f.mgr.set(e, w)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(3))

    f.mgr.get[Orientation](e).get should === (Orientation(-3 * pi / 4))
  }
}
