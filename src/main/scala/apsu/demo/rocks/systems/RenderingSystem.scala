package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import java.awt.Graphics2D
import apsu.demo.rocks.components.{Orientation, Position, Renderable}
import org.apache.log4j.Logger
import java.awt.geom.AffineTransform

/**
 * RenderingSystem
 *
 * @author david
 */
class RenderingSystem(mgr: EntityManager, doPaint: ((Graphics2D) => Unit) => Unit) extends System {

  private val log = Logger.getLogger(classOf[RenderingSystem])

  override def nickname: String = "Rendering"

  // TODO clean up all these "case _ =>" s

  override def processTick(deltaMicros: Long): Unit = {
    doPaint({
      g2 =>
        mgr.all[Renderable].foreach({
          case (e, r) =>
            val img = r.img
            mgr.get[Position](e) match {
              case Some(p) =>
                val o = mgr.get[Orientation](e).getOrElse(Orientation(0))

                g2.drawString(s"(${Math.round(p.x)}, ${Math.round(p.y)})", p.x.asInstanceOf[Float], p.y.asInstanceOf[Float])

                val tx = new AffineTransform()
                tx.setToIdentity()
                tx.translate(p.x - r.width * 0.5, p.y - r.height * 0.5)
                tx.scale(r.scaleX, r.scaleY)
                tx.rotate(o.theta, img.getWidth * 0.5, img.getHeight * 0.5)

                g2.drawImage(img, tx, null)

                log.debug(s"Rendered ${mgr.getNickname(e).getOrElse("")} at ${p} with ${o}")
              case _ =>
            }
        })
    })
  }
}
