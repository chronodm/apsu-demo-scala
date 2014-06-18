package apsu.demo.rocks

import apsu.core.MapEntityManager
import java.util.concurrent.TimeUnit
import javax.swing.JComponent
import java.awt.event.WindowAdapter
import java.awt._
import apsu.demo.rocks.components._
import apsu.demo.rocks.systems._
import org.apache.log4j.Logger
import apsu.demo.rocks.shared.MainWindow
import apsu.demo.rocks.components.World
import java.lang.Thread.UncaughtExceptionHandler

/**
 * Rocks
 *
 * @author david
 */
class Rocks(mainWindow: MainWindow) {

  // ------------------------------------------------------
  // Constants

  val updatesPerSecond = 60
  val skipNanos = TimeUnit.SECONDS.toNanos(1) / updatesPerSecond
  val maxFrameskip = 10

  // ------------------------------------------------------
  // Immutable fields

  private val log = Logger.getLogger(classOf[Rocks])
  private val mgr = new MapEntityManager

  // ------------------------------------------------------
  // Mutable fields

  var stopped = true

  // ------------------------------------------------------
  // Starting entities

  val world = mgr.newEntity("World")
  mgr.set(world, new World(mainWindow.getWidth, mainWindow.getHeight))

  val state = mgr.newEntity("State")
  mgr.set(state, GameState.init)

  // ------------------------------------------------------
  // Systems

  private val levelSystem = new LevelSystem(mgr)
  private val movementSystem = new MovementSystem(mgr)
  private val rotationSystem = new RotationSystem(mgr)
  private val renderingSystem = new RenderingSystem(mgr, mainWindow)
  private val keyboardInputSystem = {
    val kis = new KeyboardInputSystem
    KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventDispatcher(kis)
    kis
  }

  // ------------------------------------------------------
  // Methods

  def stop() {
    stopped = true
  }

  def running = {
    !stopped
  }

  def update(deltaMicros: Long) {
    levelSystem.processTick(deltaMicros)
    keyboardInputSystem.processTick(deltaMicros)
    movementSystem.processTick(deltaMicros)
    rotationSystem.processTick(deltaMicros)
  }

  def render(lastDelta: Long) {
    log.trace(s"render() at ${System.nanoTime()}")
    renderingSystem.processTick(lastDelta)
  }

  def start() {
    stopped = false

    var lastUpdate = System.nanoTime()
    var nextUpdate = lastUpdate

    var lastRender = lastUpdate

    var lastDelta = 0L

    while (running) {
      var loops = 0
      while (System.nanoTime() > nextUpdate && loops < maxFrameskip) {
        val now: Long = System.nanoTime()
        val deltaMicros = TimeUnit.NANOSECONDS.toMicros(now - lastUpdate)
        lastDelta = deltaMicros
        lastUpdate = now
        update(deltaMicros)
        nextUpdate += skipNanos
        loops += 1
      }

      // TODO move 'when to render' smarts into RenderingSystem & just treat as another system
      // TODO interpolation
      render(lastDelta)

      val now = System.nanoTime()
      log.trace(s"Rendered at $now (${TimeUnit.NANOSECONDS.toMillis(now - lastRender)} ms)")
      lastRender = System.nanoTime()
    }

  }
}

object Rocks {
  private val log = Logger.getLogger(classOf[Rocks])

  def main(args: Array[String]) {

    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler {
      override def uncaughtException(t: Thread, e: Throwable): Unit = {
        log.error(e)
        System.err.println(e)
        System.exit(1)
      }
    })

    val mainWindow = new MainWindow(false)
    mainWindow.show()

    val rocks = new Rocks(mainWindow)
    rocks.start()


  }
}
