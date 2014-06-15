package apsu.demo.rocks.systems

import org.scalatest.{fixture, Matchers}
import org.scalatest.mock.MockitoSugar
import apsu.core.{MapEntityManager, EntityManager}
import apsu.demo.rocks.components.{Position, Velocity}
import java.util.concurrent.TimeUnit
import org.scalautils.Equality
import java.math.MathContext

/**
 * MovementSystemSpec
 *
 * @author david
 */
class MovementSystemSpec extends fixture.FlatSpec with Matchers with MockitoSugar {

  case class F(mgr: EntityManager, sys: MovementSystem)

  /** Check position equality to 3 significant digits */
  implicit val positionEq = new Equality[Position] {

    private def closeEnough(d1: Double, d2: Double): Boolean = {
      val mc = new MathContext(3)
      val bd1 = BigDecimal(d1).round(mc)
      val bd2 = BigDecimal(d2).round(mc)
      bd1 == bd2
    }

    override def areEqual(p1: Position, a: Any): Boolean = {
      a match {
        case p2: Position =>
          closeEnough(p1.x, p2.x) && closeEnough(p1.y, p2.y)
        case _ => false
      }
    }
  }

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
}
