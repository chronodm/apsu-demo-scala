package apsu.demo.rocks.systems

import org.scalatest.{Matchers, fixture}
import apsu.core.{MapEntityManager, EntityManager}
import apsu.demo.testutils.OrientationEquality
import apsu.demo.rocks.components.{AngularVelocity, Orientation}
import java.util.concurrent.TimeUnit

/**
 * RotationSystemSpec
 *
 * @author david
 */
class RotationSystemSpec extends fixture.FlatSpec with Matchers {

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
    val o = Orientation(Math.PI/2)
    val w = AngularVelocity(Math.PI/4)
    f.mgr.set(e, o)
    f.mgr.set(e, w)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(1))

    f.mgr.get[Orientation](e).get should === (Orientation(3 * Math.PI / 4))
  }

  it should "take time delta into account" in { f =>
    val e = f.mgr.newEntity()
    val o = Orientation(0)
    val w = AngularVelocity(-Math.PI/4)
    f.mgr.set(e, o)
    f.mgr.set(e, w)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(3))

    f.mgr.get[Orientation](e).get should === (Orientation(-3 * Math.PI / 4))
  }
}