package apsu.demo.rocks

import apsu.core.MapEntityManager
import java.util.concurrent.TimeUnit
import javax.swing.JComponent
import java.awt.event.KeyEvent
import java.awt._
import apsu.demo.rocks.components._
import apsu.demo.rocks.systems.{RotationSystem, MovementSystem, RenderingSystem}
import apsu.demo.rocks.components.Position
import apsu.demo.rocks.components.World
import org.apache.log4j.Logger

/**
 * Rocks
 *
 * @author david
 */
class Rocks(bounds: Rectangle, doPaint: ((Graphics2D) => Unit) => Unit) {

  // ------------------------------------------------------
  // Constants

  val updatesPerSecond = 30
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
  mgr.set(world, new World(bounds.width, bounds.height))

  val rock = mgr.newEntity("Rock")
  mgr.set(rock, Renderable(96, 96, "/sprites/rock/rock-lg-384-n.png"))
  mgr.set(rock, Position(bounds.getWidth / 2, bounds.getHeight / 2))
  mgr.set(rock, Orientation(0))
  mgr.set(rock, Velocity(-50, 75))
  mgr.set(rock, AngularVelocity(1))

  // ------------------------------------------------------
  // Systems

  private val movementSystem = new MovementSystem(mgr)
  private val rotationSystem = new RotationSystem(mgr)
  private val renderingSystem = new RenderingSystem(mgr, doPaint)

  // ------------------------------------------------------
  // Methods

  def stop() {
    stopped = true
  }

  def running = {
    !stopped
  }

  def update(deltaMicros: Long) {
    log.debug(s"Update at ${System.nanoTime()}, $deltaMicros us, (${1e6 / deltaMicros} FPS)")
    movementSystem.processTick(deltaMicros)
    rotationSystem.processTick(deltaMicros)
  }

  def render() {
    log.debug(s"render() at ${System.nanoTime()}")
    renderingSystem.processTick(0)
  }

  def start() {
    stopped = false

    var lastUpdate = System.nanoTime()
    var nextUpdate = lastUpdate

    var lastRender = lastUpdate

    while (running) {
      var loops = 0
      while (System.nanoTime() > nextUpdate && loops < maxFrameskip) {
        val now: Long = System.nanoTime()
        val deltaMicros = TimeUnit.NANOSECONDS.toMicros(now - lastUpdate)
        lastUpdate = now
        update(deltaMicros)
        nextUpdate += skipNanos
        loops += 1
      }

      // TODO move 'when to render' smarts into RenderingSystem & just treat as another system
      // TODO interpolation
      render()

      val now = System.nanoTime()
      log.debug(s"Rendered at $now (${TimeUnit.NANOSECONDS.toMillis(now - lastRender)} ms)")
      lastRender = System.nanoTime()

//      var now = System.nanoTime()
//      var nextUpdate = now
//      var lastUpdate = now
//
//      for (i <- 0 until maxFrameskip if now < nextUpdate) {
//        val deltaMicros = TimeUnit.NANOSECONDS.toMicros(now - lastUpdate)
//        update(deltaMicros)
//        lastUpdate = now
//        nextUpdate += skipNanos
//        now = System.nanoTime()
//      }
//
//      // TODO move 'when to render' smarts into RenderingSystem & just treat as another system
//      render()
    }

  }
}

object Rocks {
  private val menuKeyMask = Toolkit.getDefaultToolkit.getMenuShortcutKeyMask

  def main(args: Array[String]) {
    val f = new Frame()
    f.setBackground(Color.BLACK)
    f.setResizable(false)
    f.pack()

    f.setSize(640, 480)
    f.setLocationByPlatform(true)
    f.setVisible(true)

//    f.setUndecorated(true)
//    val env = GraphicsEnvironment.getLocalGraphicsEnvironment
//    val dev = env.getDefaultScreenDevice
//    dev.setFullScreenWindow(f)

    f.createBufferStrategy(2)
    val bufferStrategy = f.getBufferStrategy

    val bounds: Rectangle = f.getBounds

    def doPaint(p: (Graphics2D) => Unit) = {
      val g2 = bufferStrategy.getDrawGraphics.asInstanceOf[Graphics2D]
      try {
        g2.clearRect(0, 0, bounds.width, bounds.height)
        p(g2)
      } finally {
        g2.dispose()
      }
      bufferStrategy.show()
    }

    val rocks = new Rocks(bounds, doPaint)

    def quit() = {
      rocks.stop()
      System.exit(0)
    }

    // TODO why doesn't this work?
    KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventDispatcher(
      new KeyEventDispatcher {
        override def dispatchKeyEvent(e: KeyEvent): Boolean = {
          if (e.getID == KeyEvent.KEY_RELEASED) {
            e.getKeyCode match {
              case KeyEvent.VK_Q if (e.getModifiers & menuKeyMask) != 0  => quit()
              case KeyEvent.VK_ESCAPE => quit()
              case _ =>
            }
          }
          false
        }
      }
    )

    rocks.start()
  }
}