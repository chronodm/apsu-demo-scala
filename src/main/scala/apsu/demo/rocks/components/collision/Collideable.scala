package apsu.demo.rocks.components.collision

import apsu.util.{EnumRegistry, EnumLike}

/**
 * Collideable
 *
 * @author david
 */
object Collideable {
  val rock = Collideable("rock")
  val playerShip = Collideable("playerShip")
  val playerBullet = Collideable("playerBullet")

  private def collidesWith(c0: Collideable, c1: Collideable): Boolean = {
    c0 match {
      case Collideable.rock =>
        c1 == playerShip || c1 == playerBullet
      case Collideable.playerShip =>
        c1 == rock
      case Collideable.playerBullet =>
        c1 == rock
    }
  }
}

case class Collideable private(name: String) extends EnumLike[Collideable] {
  CollideableRegistry.register(this)

  def collidesWith(c1: Collideable): Boolean = {
    Collideable.collidesWith(this, c1)
  }
}

private object CollideableRegistry extends EnumRegistry[Collideable]
