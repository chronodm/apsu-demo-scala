package apsu.demo.rocks.systems

import apsu.core.{System, EntityManager}
import org.apache.log4j.Logger
import apsu.demo.rocks.components.collision.Destruction
import apsu.demo.rocks.components.sprites.PlayerShip

/**
 * DestructionSystem
 *
 * @author david
 */
class DestructionSystem (mgr: EntityManager) extends System {

  private val log = Logger.getLogger(classOf[DestructionSystem])

  override def nickname: String = "Destruction"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.all[Destruction].foreach {
      case (e, d) =>
        mgr.get[PlayerShip](e) match {
          case Some(ps) =>

          case _ => // TODO clean up all these "case _ =>" s
        }
        mgr.delete(e)
    }
  }
}
