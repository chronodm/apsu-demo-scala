package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import apsu.demo.rocks.components.{Velocity, Position}
import java.util.concurrent.TimeUnit

/**
 * MovementSystem
 *
 * @author david
 */
class MovementSystem(mgr: EntityManager) extends System {

  // Multiplication is faster than division
  private val secondsPerMicro = 1e-6

  override def nickname: String = "Movement"

  override def processTick(delta: Long): Unit = {
    mgr.all[Velocity].foreach[Unit]({ case (e, v) =>
        mgr.get[Position](e) match {
          case Some(p0) =>
            val dx = delta * (v.x * secondsPerMicro)
            val dy = delta * (v.y * secondsPerMicro)
            val p1 = Position(p0.x + dx, p0.y + dy)
            mgr.set(e, p1)
        }
    })
  }
}
