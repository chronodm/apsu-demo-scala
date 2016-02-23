package apsu.demo.rocks

import apsu.core.MapEntityManager
import java.util.concurrent.TimeUnit
import java.awt._
import apsu.demo.rocks.components._
import apsu.demo.rocks.systems._
import org.apache.log4j.Logger
import apsu.demo.rocks.shared.MainWindow
import java.lang.Thread.UncaughtExceptionHandler
import apsu.demo.rocks.components.rendering.World

/**
 * Rocks
 *
 * @author david
 */
class Rocks(mainWindow: MainWindow) {

  // ------------------------------------------------------
  // Constants

  val updatesPerSecond = 90
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

  private val renderingSystem = new RenderingSystem(mgr, mainWindow)
  private val keyboardInputSystem = {
    val kis = new KeyboardInputSystem(mgr)
    KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventDispatcher(kis)
    kis
  }

  private val systems = scala.collection.immutable.List[apsu.core.System](
    new LevelSystem(mgr),
    keyboardInputSystem,
    new CommandSystem(mgr),
    new MovementSystem(mgr),
    new RotationSystem(mgr),
    new FrictionSystem(mgr),
    new CollisionDetectionSystem(mgr),
    new CollisionSystem(mgr),
    new DestructionSystem(mgr)
  )

  // ------------------------------------------------------
  // Methods

  def stop(): Unit = {
    stopped = true
  }

  def running = {
    !stopped
  }

  def update(deltaMicros: Long): Unit = {
    for (system <- systems) {
      system.processTick(deltaMicros)
    }
  }

  def render(lastDelta: Long): Unit = {
    log.trace(s"render() at ${System.nanoTime()}")
    renderingSystem.processTick(lastDelta)
  }

  def start(): Unit = {
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

  def main(args: Array[String]): Unit = {

    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler {
      override def uncaughtException(t: Thread, e: Throwable): Unit = {
        log.error(e.getMessage, e)
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
