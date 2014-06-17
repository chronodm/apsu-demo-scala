package apsu.demo.rocks.systems

import apsu.core.{Entity, EntityManager, System}
import apsu.demo.rocks.components.{World, Velocity, Position}
import apsu.core.System.secondsPerMicro
import org.apache.log4j.Logger

/**
 * MovementSystem
 *
 * @author david
 */
class MovementSystem(mgr: EntityManager) extends System {

  private val log = Logger.getLogger(classOf[MovementSystem])

  override def nickname: String = "Movement"

  override def processTick(deltaMicros: Long): Unit = {

    val world: Option[(Entity, World)] = mgr.all[World].headOption

    mgr.all[Velocity].foreach({
      case (e, v) =>
        val p0 = mgr.get[Position](e).getOrElse(Position(0, 0))
        val deltaSeconds = deltaMicros * secondsPerMicro
        val dx = deltaSeconds * v.x
        val dy = deltaSeconds * v.y

        val newX: Double = p0.x + dx
        val newY: Double = p0.y + dy

        // wrap to world if present
        val p1 = world match {
          case Some((_, w)) =>
            val x1 = wrapIfNeeded(newX, w.width)
            val y1 = wrapIfNeeded(newY, w.height)
            Position(x1, y1)
          case _ => Position(newX, newY)
        }

        log.debug(s"Moving ${mgr.getNickname(e).getOrElse("")} from $p0 to $p1 ($v)")

        mgr.set(e, p1)
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
