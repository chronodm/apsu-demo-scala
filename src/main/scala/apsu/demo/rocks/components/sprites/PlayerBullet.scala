package apsu.demo.rocks.components.sprites

import apsu.demo.rocks.components.rendering.{World, Renderable}
import apsu.core.EntityManager
import apsu.demo.rocks.components.geometry.{DeleteAtEdge, Orientation, Position, Velocity}
import apsu.demo.rocks.components.collision.Collideable

/**
 * PlayerBullet
 *
 * @author david
 */
object PlayerBullet {
  val renderable = Renderable(8, 4, "/sprites/ship/bullet-32x16-e.png")

  private val speed = 400

  def add(shipVelocity: Velocity, shipPosition: Position, shipOrientation: Orientation, mgr: EntityManager) = {
    val bullet = mgr.newEntity()
    mgr.setNickname(bullet, s"Bullet ${bullet.id.toString}")
    mgr.set(bullet, this)
    mgr.set(bullet, renderable)
    mgr.set(bullet, shipPosition) // TODO don't generate right on top of player
    mgr.set(bullet, shipOrientation)
    mgr.set(bullet, shipVelocity + Velocity.fromPolar(speed, shipOrientation.theta))
    mgr.set(bullet, Collideable.playerBullet)
    mgr.set(bullet, DeleteAtEdge())
  }
}
