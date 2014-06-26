package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import apsu.core.System.secondsPerMicro
import org.apache.log4j.Logger
import apsu.demo.rocks.components.geometry.{Orientation, AngularVelocity}

/**
 * RotationSystem
 *
 * @author david
 */
class RotationSystem(mgr: EntityManager) extends System {
  private val log = Logger.getLogger(classOf[RotationSystem])

  override def nickname: String = "Rotation"

  override def processTick(deltaMicros: Long): Unit = {
    for ((e, w) <- mgr.all[AngularVelocity]) {
      val o0 = mgr.get[Orientation](e).getOrElse(Orientation(0))
      val deltaSeconds = deltaMicros * secondsPerMicro
      val dTheta = deltaSeconds * w.theta
      val o1 = o0 + dTheta

      log.trace(s"Rotating ${mgr.getNickname(e).getOrElse("")} from $o0 to $o1 ($w)")

      mgr.set(e, o1)
    }
  }
}
