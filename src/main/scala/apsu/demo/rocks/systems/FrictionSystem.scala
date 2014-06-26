package apsu.demo.rocks.systems

import apsu.core.{Entity, EntityManager, System}
import apsu.demo.rocks.components.geometry.{Velocity, Friction}

/**
 * FrictionSystem
 *
 * @author david
 */
class FrictionSystem(mgr: EntityManager) extends System {

  override def nickname: String = "Friction"

  override def processTick(deltaMicros: Long): Unit = {
    for ((e, f) <- mgr.all[Friction]) {
      for (v <- mgr.get[Velocity](e)) {
        val v1 = Velocity.fromPolar(v.magnitude * f.coeff, v.theta)
        mgr.set(e, v1)
      }
    }
  }
}
