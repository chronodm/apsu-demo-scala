package apsu.demo.rocks.systems

import apsu.core.System
import java.awt.KeyEventDispatcher
import java.awt.event.KeyEvent
import scala.collection.immutable.Queue
import org.apache.log4j.Logger

/**
 * KeyboardInputHandler
 *
 * @author david
 */
class KeyboardInputSystem extends System with KeyEventDispatcher {

  private val log = Logger.getLogger(classOf[KeyboardInputSystem])

  override def nickname: String = "KeyboardInput"

  val empty = Queue[KeyEvent]()
  var queue = Queue[KeyEvent]()

  override def processTick(deltaMicros: Long): Unit = {
    val pending = queue
    for (e <- pending) {
      e.getKeyCode match {
        case KeyEvent.VK_ESCAPE => java.lang.System.exit(0)
        case _ =>
      }
    }
    queue = empty
  }

  override def dispatchKeyEvent(e: KeyEvent): Boolean = {
    if (e.getID == KeyEvent.KEY_RELEASED) {
      queue = queue.enqueue(e)
    }
    false
  }

}
