package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import java.awt.Graphics2D
import apsu.demo.rocks.components.{Orientation, Position, Renderable}
import java.awt.geom.AffineTransform
import org.apache.log4j.{Level, Logger}

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
            val xScale = r.width / img.getWidth(null)
            val yScale = r.height / img.getWidth(null)
            mgr.get[Position](e) match {
              case Some(p) =>
                val tx = AffineTransform.getScaleInstance(xScale, yScale)
                tx.translate(p.x, p.y)

                val o = mgr.get[Orientation](e).getOrElse(Orientation(0))
//                tx.rotate(o.theta)

                // TODO use renderable bounds
                g2.drawImage(img, tx, null)

                log.debug(s"Rendered ${mgr.getNickname(e).getOrElse("")} at ${p} with ${o}")
              case _ =>
            }
        })
    })
  }
}
