package apsu.demo.rocks.components.sprites

import apsu.core.EntityManager
import apsu.demo.rocks.components.rendering.{Renderable, World}
import apsu.demo.rocks.components.geometry.{Friction, Orientation, Position}
import apsu.demo.rocks.components.collision.Collideable

/**
 * Marker component for the player's ship
 *
 * @author david
 */
case class PlayerShip(player: Int) {



  def add(mgr: EntityManager, w: World) = {
    val shipEntity = mgr.newEntity("playerShip")
    mgr.set(shipEntity, this)
    mgr.set(shipEntity, PlayerShip.defaultPosition(w))
    mgr.set(shipEntity, PlayerShip.defaultOrientation)
    mgr.set(shipEntity, PlayerShip.renderable)
    mgr.set(shipEntity, Friction(0.99f))
    mgr.set(shipEntity, Collideable.playerShip)
  }

}

object PlayerShip {
  val renderable = Renderable(24, 24, "/sprites/ship/ship-96-e.png")

  val defaultOrientation: Orientation = Orientation(-0.5 * Math.PI)

  def defaultPosition(w: World): Position = {
    Position(w.width / 2, w.height / 2)
  }

}
