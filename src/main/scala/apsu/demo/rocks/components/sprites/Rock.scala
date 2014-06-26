package apsu.demo.rocks.components.sprites

import apsu.demo.rocks.util.{EnumRegistry, EnumLike}
import apsu.demo.rocks.components.rendering.{World, Renderable}
import apsu.core.EntityManager
import apsu.demo.rocks.components.geometry.{AngularVelocity, Velocity, Orientation, Position}
import apsu.demo.rocks.components.collision.Collideable

/**
 * RockSize
 *
 * @author david
 */
object Rock {
  val small = Rock("small")
  val medium = Rock("medium")
  val large = Rock("large")

  private val renderables = Map(
    small -> Renderable(24, 24, "/sprites/rock/rock-sm-96-n.png"),
    medium -> Renderable(48, 48, "/sprites/rock/rock-md-192-n.png"),
    large -> Renderable(64, 64, "/sprites/rock/rock-lg-256-n.png")
  )

  private val speeds = Map( small -> 200, medium -> 100, large -> 50 )

  private val spins = Map( small -> 4, medium -> 2, large -> 1)

  private val childSizes = Map(medium -> small, large -> medium)
}

case class Rock private(name: String) extends EnumLike[Rock] {

  RockSizeRegistry.register(this)

  lazy val renderable = Rock.renderables(this)
  lazy val speed = Rock.speeds(this)
  lazy val spin = Rock.spins(this)
  lazy val childSize = Rock.childSizes.get(this)

  def add(vTheta: Float, position: Position, clockwise: Boolean, mgr: EntityManager) {
    val rock = mgr.newEntity()
    mgr.setNickname(rock, s"Rock ${rock.id.toString}")
    mgr.set(rock, this)
    mgr.set(rock, renderable)
    mgr.set(rock, position)
    mgr.set(rock, Orientation(0))
    mgr.set(rock, Velocity.fromPolar(speed, vTheta))
    mgr.set(rock, if (clockwise) AngularVelocity(-spin) else AngularVelocity(spin))
    mgr.set(rock, Collideable.rock)
  }


}

private object RockSizeRegistry extends EnumRegistry[Rock]
