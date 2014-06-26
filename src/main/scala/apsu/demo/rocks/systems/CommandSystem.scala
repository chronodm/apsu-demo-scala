package apsu.demo.rocks.systems

import apsu.core.{System, Entity, EntityManager}
import apsu.demo.rocks.components.command.CommandQueue
import apsu.demo.rocks.components.geometry.{Position, Velocity, Orientation}
import apsu.demo.rocks.components.command.Command
import org.apache.log4j.Logger
import apsu.demo.rocks.components.sprites.{PlayerBullet, PlayerShip}

/**
 * CommandSystem
 *
 * @author david
 */
class CommandSystem(mgr: EntityManager) extends System {

  private val log = Logger.getLogger(classOf[CommandSystem])

  private val vMax = 500
  private val accIncrement: Float = 10
  private val oIncrement = (Math.PI / 24).asInstanceOf[Float]

  override def nickname: String = "CommandSystem"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.forAll[CommandQueue] { (e, cq) =>
      for (c <- cq.commands) {
        log.trace(s"Invoking: $c")
        c match {
          case Command.accelerate =>
            accelerate(e)
          case Command.rotateClockwise =>
            rotateCW(e)
          case Command.rotateCounterclockwise =>
            rotateCCW(e)
          case Command.fire =>
            fire(e)
          case Command.exit =>
            java.lang.System.exit(0)
        }
      }
      mgr.remove[CommandQueue](e)
    }
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

  def fire(e: Entity) {
    val v0 = mgr.get[Velocity](e).getOrElse(Velocity(0, 0))
    val p0 = mgr.get[Position](e).getOrElse(Position(0, 0)) // TODO use ship default position
    val o0 = mgr.get[Orientation](e).getOrElse(PlayerShip.defaultOrientation)

    PlayerBullet.add(v0, p0, o0, mgr)
  }
}
