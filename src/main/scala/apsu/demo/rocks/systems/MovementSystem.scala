package apsu.demo.rocks.systems

import apsu.core.{Entity, EntityManager, System}
import apsu.demo.rocks.components.{Screen, Velocity, Position}
import apsu.core.System.secondsPerMicro

/**
 * MovementSystem
 *
 * @author david
 */
class MovementSystem(mgr: EntityManager) extends System {

  override def nickname: String = "Movement"

  override def processTick(deltaMicros: Long): Unit = {

    val screen: Option[(Entity, Screen)] = mgr.all[Screen].headOption

    mgr.all[Velocity].foreach[Unit]({
      case (e, v) =>
        mgr.get[Position](e) match {
          case Some(p0) =>
            val deltaSeconds = deltaMicros * secondsPerMicro
            val dx = deltaSeconds * v.x
            val dy = deltaSeconds * v.y

            val newX: Double = p0.x + dx
            val newY: Double = p0.y + dy

            // wrap to screen if present
            val p1 = screen match {
              case Some((_, s)) =>
                val x1 = wrapIfNeeded(newX, s.width)
                val y1 = wrapIfNeeded(newY, s.height)
                Position(x1, y1)
              case _ => Position(newX, newY)
            }

            mgr.set(e, p1)
          case _ =>
        }
    })
  }

  private def wrapIfNeeded(n: Double, max: Double) = {
    if (n > max) {
      n - max
    } else if (n < 0) {
      n + max
    } else {
      n
    }
  }
}
