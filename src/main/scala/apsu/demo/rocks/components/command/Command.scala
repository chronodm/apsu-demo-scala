package apsu.demo.rocks.components.command

import java.awt.event.KeyEvent
import apsu.core.{Entity, EntityManager}
import apsu.demo.rocks.components.geometry.{Orientation, Velocity}
import apsu.util.{EnumRegistry, EnumLike}
import org.apache.log4j.Logger

/**
 * Command
 *
 * @author david
 */
object Command {

  private val log = Logger.getLogger(classOf[Command])

  // ------------------------------------------------------------
  // Commands

  val accelerate = Command("accelerate")
  val rotateClockwise = Command("rotateClockwise")
  val rotateCounterclockwise = Command("rotateCounterclockwise")
  val fire = Command("fire")

  val exit = Command("exit")

  // ------------------------------------------------------------
  // Factory method

  def apply(e: KeyEvent): Option[Command] = {
    val cmd = e.getKeyCode match {
      // TODO some sort of registry

      case KeyEvent.VK_W => Some(accelerate)
      case KeyEvent.VK_UP => Some(accelerate)

      case KeyEvent.VK_A => Some(rotateCounterclockwise)
      case KeyEvent.VK_LEFT => Some(rotateCounterclockwise)

      case KeyEvent.VK_D => Some(rotateClockwise)
      case KeyEvent.VK_RIGHT => Some(rotateClockwise)

      case KeyEvent.VK_SPACE => Some(fire)

      case KeyEvent.VK_ESCAPE => Some(exit)

      case _ => None
    }
    log.trace(s"apply($e) returning $cmd")
    cmd
  }
}

case class Command private(name: String) extends EnumLike[Command] {
  CommandRegistry.register(this)
}

private object CommandRegistry extends EnumRegistry[Command]
