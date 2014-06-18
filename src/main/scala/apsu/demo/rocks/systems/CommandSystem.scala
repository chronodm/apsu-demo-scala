package apsu.demo.rocks.systems

import apsu.core.{System, Entity, EntityManager}
import apsu.demo.rocks.components.command.CommandQueue
import apsu.demo.rocks.components.geometry.{Velocity, Orientation}
import apsu.demo.rocks.components.command.Command
import org.apache.log4j.Logger

/**
 * CommandSystem
 *
 * @author david
 */
class CommandSystem (mgr: EntityManager) extends System {

  private val log = Logger.getLogger(classOf[CommandSystem])

  private val vMax = 500

  override def nickname: String = "CommandSystem"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.all[CommandQueue].foreach({
      case (e, cq) =>
        for (c <- cq.commands) {
          log.debug(s"Invoking: $c")
          c match {
            case Command.accelerate =>
              accelerate(e)
            case Command.exit =>
              java.lang.System.exit(0)
          }
        }
        mgr.remove[CommandQueue](e)
    })
  }

  def accelerate(e: Entity) {
    val orientation = mgr.get[Orientation](e).getOrElse(Orientation(0))
    val v0 = mgr.get[Velocity](e).getOrElse(Velocity(0, 0))
    val deltaV = Velocity.fromPolar(10, orientation.theta)
    val v1: Velocity = v0 + deltaV
    val vActual = if (v1.length <= vMax) v1 else vMax
    mgr.set(e, vActual)
  }
}
