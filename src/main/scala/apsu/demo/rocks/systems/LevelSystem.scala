package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import apsu.demo.rocks.components._
import scala.Some
import java.util.Random
import apsu.demo.rocks.components.geometry._
import apsu.demo.rocks.components.rendering.{World, Renderable}
import apsu.demo.rocks.components.geometry.Position
import apsu.demo.rocks.components.rendering.World
import apsu.demo.rocks.components.rendering.Renderable
import scala.Some
import apsu.demo.rocks.components.geometry.AngularVelocity
import apsu.demo.rocks.components.collision.Collideable
import apsu.demo.rocks.components.sprites.{Rock, PlayerShip}

/**
 * LevelSystem
 *
 * @author david
 */
class LevelSystem(mgr: EntityManager) extends System  {

  // ------------------------------------------------------
  // Fields

  private val r = new Random()

  val shipRenderable: Renderable = Renderable(24, 24, "/sprites/ship/ship-96-e.png")
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
        PlayerShip(1).add(mgr, w) // always player 1
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
        Rock.large.add(
          r.nextDouble() * (2 * Math.PI),
          Position(r.nextInt(w.width), r.nextInt(w.height)),
          r.nextBoolean(),
          mgr, w)
      case _ => // TODO clean up all these "case _ =>" s
    }
  }

}
