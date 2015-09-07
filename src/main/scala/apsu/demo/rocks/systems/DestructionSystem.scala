package apsu.demo.rocks.systems

import apsu.core.{Entity, System, EntityManager}
import org.apache.log4j.Logger
import apsu.demo.rocks.components.collision.Destruction
import apsu.demo.rocks.components.sprites.{PlayerBullet, Rock, PlayerShip}
import apsu.demo.rocks.components.geometry.{Velocity, Position}

/**
 * DestructionSystem
 *
 * @author david
 */
class DestructionSystem(mgr: EntityManager) extends System {

  private val piOver2 = (Math.PI / 2).asInstanceOf[Float]

  private val log = Logger.getLogger(classOf[DestructionSystem])

  override def nickname: String = "Destruction"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.forAll[Destruction] { (e, d) =>
      // TODO this is awkward; these are exclusive. Separate systems?
      for (ps <- mgr.get[PlayerShip](e)) {
        // TODO this is a hack to remove the player ship without invalidating the exit command; we can do better
        //            mgr.delete(e)
        //            mgr.set(e, ps)
        // TODO lose life and/or game over
      }
      for (r <- mgr.get[Rock](e)) {
        split(e, r)
        mgr.delete(e)
      }
      for (pb <- mgr.get[PlayerBullet](e)) {
        mgr.delete(e)
      }
      log.trace(s"Deleting $e due to $d")
      mgr.remove[Destruction](e)
    }
  }

  private def split(e: Entity, r: Rock): Unit = {
    for (r1 <- r.childSize) {
      (mgr.get[Velocity](e), mgr.get[Position](e)) match {
        case (Some(v), Some(p)) =>
          for (vTheta <- Seq(v.theta + piOver2, v.theta - piOver2)) {
            r1.add(vTheta, p, vTheta > 0, mgr)
          }
        case _ => // TODO clean up all these "case _ =>" s
      }
    }
  }
}
