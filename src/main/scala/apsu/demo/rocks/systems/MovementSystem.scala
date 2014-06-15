package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import apsu.demo.rocks.components.{Velocity, Position}
import apsu.core.System.secondsPerMicro

/**
 * MovementSystem
 *
 * @author david
 */
class MovementSystem(mgr: EntityManager) extends System {

  override def nickname: String = "Movement"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.all[Velocity].foreach[Unit]({ case (e, v) =>
        mgr.get[Position](e) match {
          case Some(p0) =>
            val deltaSeconds = deltaMicros * secondsPerMicro
            val dx = deltaSeconds * v.x
            val dy = deltaSeconds * v.y
            val p1 = Position(p0.x + dx, p0.y + dy)
            mgr.set(e, p1)
          case _ =>
        }
    })
  }
}
