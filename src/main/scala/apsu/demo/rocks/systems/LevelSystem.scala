package apsu.demo.rocks.systems

import apsu.core.{Entity, EntityManager, System}
import apsu.demo.rocks.components._
import apsu.demo.rocks.components.Position
import apsu.demo.rocks.components.World
import apsu.demo.rocks.components.Renderable
import scala.Some
import java.util.Random

/**
 * LevelSystem
 *
 * @author david
 */
class LevelSystem(mgr: EntityManager) extends System  {

  // ------------------------------------------------------
  // Fields

  private val r = new Random()

  val shipRenderable: Renderable = Renderable(24, 24, "/sprites/ship/ship-96-n.png")
  val rockRenderable: Renderable = Renderable(64, 64, "/sprites/rock/rock-lg-256-n.png")

  val numRocks = 6
  val rockSpeed = 50
  val rockSpin = 1

  // ------------------------------------------------------
  // System

  override def nickname: String = "Level"

  override def processTick(deltaMicros: Long): Unit = {
    mgr.all[GameState].headOption match {
      case Some((state, GameState.init)) =>
        initPlayerShip()
        initRocks()
        mgr.set(state, GameState.playing)
      case _ => // TODO clean up all these "case _ =>" s
    }
  }

  // ------------------------------------------------------
  // Methods

  def initPlayerShip() {
    mgr.all[World].headOption match {
      case Some((_, w)) =>
        val shipEntity = mgr.newEntity("playerShip")
        mgr.set(shipEntity, PlayerShip(1)) // always player 1
        mgr.set(shipEntity, Position(w.width / 2, w.height / 2))
        mgr.set(shipEntity, shipRenderable)
      case _ => // TODO clean up all these "case _ =>" s
    }
  }

  def initRocks() {
    for (i <- 0 until numRocks) {
      addRock()
    }
  }

  def addRock() {
    // TODO avoid player ship collisions on startup
    mgr.all[World].headOption match {
      case Some((_, w)) =>
        val rock = mgr.newEntity()
        mgr.setNickname(rock, s"Rock ${rock.id.toString}")

        mgr.set(rock, rockRenderable)
        mgr.set(rock, Position(r.nextInt(w.width), r.nextInt(w.height)))
        mgr.set(rock, Orientation(0))
        mgr.set(rock, randomVelocity(rockSpeed))
        mgr.set(rock, AngularVelocity(rockSpin))
      case _ => // TODO clean up all these "case _ =>" s
    }
  }

  def randomVelocity(magnitude: Double) = {
    val vTheta = r.nextDouble() * (2 * Math.PI)
    val vX = Math.cos(vTheta) * magnitude
    val vY = Math.sin(vTheta) * magnitude
    Velocity(vX, vY)
  }
}
