package apsu.demo.rocks.systems

import apsu.core.{Entity, EntityManager, System}
import apsu.core.System.secondsPerMicro
import org.apache.log4j.Logger
import apsu.demo.rocks.components.geometry.{DeleteAtEdge, Position, Velocity}
import apsu.demo.rocks.components.rendering.World

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

    mgr.forAll[Velocity] { (e, v) =>
      for (p0 <- mgr.get[Position](e)) {
        val deltaSeconds = deltaMicros * secondsPerMicro
        val dx = deltaSeconds * v.x
        val dy = deltaSeconds * v.y

        val newX: Float = p0.x + dx
        val newY: Float = p0.y + dy

        // wrap to world if present
        val p1 = world match {
          case Some((_, w)) =>
            val x1 = wrapIfNeeded(newX, w.width)
            val y1 = wrapIfNeeded(newY, w.height)
            Position(x1, y1)
          case _ => Position(newX, newY)
        }

        log.trace(s"Moving ${mgr.getNickname(e).getOrElse("")} from $p0 to $p1 ($v)")

        val didWrap = p1.x != newX || p1.y != newY
        mgr.get[DeleteAtEdge](e) match {
          case Some(_) if didWrap =>
            mgr.delete(e)
          case _ =>
            mgr.set(e, p1)
        }
      }
    }
  }

  private def wrapIfNeeded(n: Float, max: Float) = {
    if (n > max) {
      n - max
    } else if (n < 0) {
      n + max
    } else {
      n
    }
  }
}
