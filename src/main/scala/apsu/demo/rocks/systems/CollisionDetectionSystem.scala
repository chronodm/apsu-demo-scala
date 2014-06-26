package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import apsu.demo.rocks.components.collision.{Collision, Collideable}
import apsu.demo.rocks.components.geometry.Position
import apsu.demo.rocks.components.rendering.Renderable
import org.apache.log4j.Logger

/**
 * CollisionDetectionSystem
 *
 * @author david
 */
case class CollisionDetectionSystem(mgr: EntityManager) extends System {

  private val log = Logger.getLogger(classOf[CollisionDetectionSystem])

  override def nickname: String = "Collision Detection"

  override def processTick(deltaMicros: Long): Unit = {
    // TODO find a way to do this that's not quadratic (better data?)
    for ((e, c) <- mgr.all[Collideable]) {
      for ((e1, c1) <- mgr.all[Collideable]) {
        if (e != e1 && c.collidesWith(c1)) {
          (mgr.get[Position](e), mgr.get[Position](e1), mgr.get[Renderable](e), mgr.get[Renderable](e1)) match {
            case (Some(p), Some(p1), Some(r), Some(r1)) =>
              val distMin = r.radius + r1.radius
              val dist = p.distanceTo(p1)
              if (dist < distMin) {
                log.trace(s"Colliding: $c ($e), $c1 ($e1)")
                mgr.set(e, Collision(c, c1))
                mgr.set(e1, Collision(c1, c))
              }
            case _ => // TODO clean up all these "case _ =>" s
          }
        }
      }
    }
  }
}
