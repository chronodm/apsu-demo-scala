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
  private val accIncrement: Double = 10
  private val oIncrement = Math.PI / 24

  override def nickname: String = "CommandSystem"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.all[CommandQueue].foreach({
      case (e, cq) =>
        for (c <- cq.commands) {
          log.trace(s"Invoking: $c")
          c match {
            case Command.accelerate =>
              accelerate(e)
            case Command.rotateClockwise =>
              rotateCW(e)
            case Command.rotateCounterclockwise =>
              rotateCCW(e)
            case Command.exit =>
              java.lang.System.exit(0)
          }
        }
        mgr.remove[CommandQueue](e)
    })
  }

  // TODO maybe Command shouldn't be a case class / enum and these should be encapsulated?

  def accelerate(e: Entity) {
    val orientation = mgr.get[Orientation](e).getOrElse(Orientation(0))
    val v0 = mgr.get[Velocity](e).getOrElse(Velocity(0, 0))
    val deltaV = Velocity.fromPolar(accIncrement, orientation.theta)
    val v1: Velocity = v0 + deltaV
    val vActual = if (Math.abs(v1.magnitude) <= vMax) {
      v1
    } else {
      Velocity.fromPolar(Math.copySign(vMax, v1.magnitude), v1.theta)
    }

    log.trace(s"accelerate($e): $v0 + $deltaV = $v1 (-> $vActual)")

    mgr.set(e, vActual)
  }

  def rotateCW(e: Entity) {
    val o0 = mgr.get[Orientation](e).getOrElse(Orientation(0))
    val o1 = o0 + oIncrement
    mgr.set(e, o1)
  }

  def rotateCCW(e: Entity) {
    val o0 = mgr.get[Orientation](e).getOrElse(Orientation(0))
    val o1 = o0 - oIncrement
    mgr.set(e, o1)
  }
}
