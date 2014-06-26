package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import java.awt.KeyEventDispatcher
import java.awt.event.KeyEvent
import scala.collection.immutable.Queue
import org.apache.log4j.Logger
import apsu.demo.rocks.components.command.{CommandQueue, Command}
import apsu.demo.rocks.components.sprites.PlayerShip

/**
 * KeyboardInputHandler
 *
 * @author david
 */
class KeyboardInputSystem(mgr: EntityManager) extends System with KeyEventDispatcher {

  private val log = Logger.getLogger(classOf[KeyboardInputSystem])

  override def nickname: String = "KeyboardInput"

  val empty = Queue[KeyEvent]()
  var queue = Queue[KeyEvent]()

  override def processTick(deltaMicros: Long): Unit = {
    // TODO figure out how to make this atomic
    val pending = queue
    queue = empty

    if (!pending.isEmpty) log.trace(s"Found ${pending.size} pending key events in queue")

    // TODO separate player and ship
    val commands = (for (e <- pending) yield Command(e)).flatten
    mgr.forAll[PlayerShip] { (e, s) =>
      if (!commands.isEmpty) log.trace(s"Enqueuing ${commands.size} commands")
      mgr.get[CommandQueue](e) match {
        case Some(cq0) =>
          mgr.set(e, cq0 + commands)
        case _ =>
          mgr.set(e, CommandQueue(commands))
      }
    }
  }

  // TODO support n-key rollover
  override def dispatchKeyEvent(e: KeyEvent): Boolean = {
    log.trace(s"dispatchKeyEvent($e)")
    if (e.getID == KeyEvent.KEY_PRESSED) {
      log.trace(s"Enqueuing $e")
      queue = queue.enqueue(e)
    }
    false
  }

}
