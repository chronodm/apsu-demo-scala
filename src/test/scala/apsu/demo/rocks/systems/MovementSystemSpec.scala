package apsu.demo.rocks.systems

import org.scalatest.{fixture, Matchers}
import apsu.core.{MapEntityManager, EntityManager}
import apsu.demo.rocks.components.{Screen, Position, Velocity}
import java.util.concurrent.TimeUnit
import apsu.demo.testutils.PositionEquality

/**
 * MovementSystemSpec
 *
 * @author david
 */
class MovementSystemSpec extends fixture.FlatSpec with Matchers {

  case class F(mgr: EntityManager, sys: MovementSystem)

  implicit val positionEq = new PositionEquality

  type FixtureParam = F

  def withFixture(test: OneArgTest) = {
    val mgr = new MapEntityManager
    val sys = new MovementSystem(mgr)
    val f = F(mgr, sys)
    withFixture(test.toNoArgTest(f))
  }

  "processTick" should "adjust position" in { f =>
    val e = f.mgr.newEntity()
    val p = Position(1, 1)
    val v = Velocity(2, 3)
    f.mgr.set(e, p)
    f.mgr.set(e, v)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(1))

    f.mgr.get[Position](e) should be(Some(Position(3, 4)))
  }

  it should "take time delta into account" in { f =>
    val e = f.mgr.newEntity()
    val p = Position(1, 1)
    val v = Velocity(2, 3)
    f.mgr.set(e, p)
    f.mgr.set(e, v)

    val delta = TimeUnit.SECONDS.toMicros(1) / 60L
    f.sys.processTick(delta)

    val expected = Position(1 + 2 / 60.0, 1 + 3 / 60.0)
    f.mgr.get[Position](e).get should === (expected)
  }

  it should "ignore anomalous entities with velocity but no position" in { f =>
    val e = f.mgr.newEntity()
    val v = Velocity(2, 3)
    f.mgr.set(e, v)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(1))
    f.mgr.get[Position](e) should be(None)
  }

  it should "wrap north to south" in { f =>
    val screenEntity = f.mgr.newEntity()
    val screen = Screen(16, 9)
    f.mgr.set(screenEntity, screen)

    val mobEntity = f.mgr.newEntity()
    val p = Position(8, 1)
    val v = Velocity(2, -2)
    f.mgr.set(mobEntity, p)
    f.mgr.set(mobEntity, v)

    val expected = Position(10, 8)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(1))
    f.mgr.get[Position](mobEntity).get should === (expected)
  }

  it should "wrap south to north" in { f =>
    val screenEntity = f.mgr.newEntity()
    val screen = Screen(16, 9)
    f.mgr.set(screenEntity, screen)

    val mobEntity = f.mgr.newEntity()
    val p = Position(8, 8)
    val v = Velocity(2, 2)
    f.mgr.set(mobEntity, p)
    f.mgr.set(mobEntity, v)

    val expected = Position(10, 1)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(1))
    f.mgr.get[Position](mobEntity).get should === (expected)
  }

  it should "wrap east to west" in { f =>
    val screenEntity = f.mgr.newEntity()
    val screen = Screen(16, 9)
    f.mgr.set(screenEntity, screen)

    val mobEntity = f.mgr.newEntity()
    val p = Position(15, 4)
    val v = Velocity(2, 2)
    f.mgr.set(mobEntity, p)
    f.mgr.set(mobEntity, v)

    val expected = Position(1, 6)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(1))
    f.mgr.get[Position](mobEntity).get should === (expected)
  }

  it should "wrap west to east" in { f =>
    val screenEntity = f.mgr.newEntity()
    val screen = Screen(16, 9)
    f.mgr.set(screenEntity, screen)

    val mobEntity = f.mgr.newEntity()
    val p = Position(1, 4)
    val v = Velocity(-2, 2)
    f.mgr.set(mobEntity, p)
    f.mgr.set(mobEntity, v)

    val expected = Position(15, 6)

    f.sys.processTick(TimeUnit.SECONDS.toMicros(1))
    f.mgr.get[Position](mobEntity).get should === (expected)
  }
}
