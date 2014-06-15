package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import apsu.core.System.secondsPerMicro
import apsu.demo.rocks.components.{Orientation, AngularVelocity}

/**
 * RotationSystem
 *
 * @author david
 */
class RotationSystem(mgr: EntityManager) extends System {
  override def nickname: String = "Rotation"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.all[AngularVelocity].foreach[Unit]({ case (e, w) =>
        mgr.get[Orientation](e) match {
          case Some(o0) =>
        }
    })
  }
}
