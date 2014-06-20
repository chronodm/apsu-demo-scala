package apsu.demo.rocks.systems

import apsu.core.{System, EntityManager}
import apsu.demo.rocks.components.collision.{Destruction, Collideable, Collision}
import org.apache.log4j.Logger

/**
 * CollisionSystem
 *
 * @author david
 */
class CollisionSystem(mgr: EntityManager) extends System {

  private val log = Logger.getLogger(classOf[CollisionSystem])

  override def nickname: String = "Collision"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.all[Collision].foreach {
      case (e, c) =>

        log.trace(s"Found collision $c on $e")

        c match {
          case Collision(Collideable.playerShip, Collideable.rock) |
               Collision(Collideable.playerBullet, Collideable.rock) |
               Collision(Collideable.rock, Collideable.playerBullet) =>
            log.debug(s"Setting destruction on $e for $c")
            mgr.set[Destruction](e, Destruction())
          case _ =>
        }
        mgr.remove[Collision](e)
    }
  }
}
