package apsu.demo.rocks.systems

import apsu.core.{EntityManager, System}
import java.awt.{Rectangle, Graphics2D}
import org.apache.log4j.Logger
import java.awt.geom.AffineTransform
import apsu.demo.rocks.shared.{Painter, PaintHandler}
import apsu.demo.rocks.components.geometry.{Orientation, Position}
import apsu.demo.rocks.components.rendering.Renderable

/**
 * RenderingSystem
 *
 * @author david
 */
// TODO make PaintHandler a component or something
class RenderingSystem(mgr: EntityManager, paintHandler: PaintHandler) extends System {

  private val log = Logger.getLogger(classOf[RenderingSystem])

  override def nickname: String = "Rendering"

  override def processTick(lastDelta: Long): Unit = {

    val painter = new Painter {
      override def paint(g2: Graphics2D, bounds: Rectangle): Unit = {
        //        val frameRate = Math.round(1e6 / lastDelta)
        //        g2.drawString(s"FPS: $frameRate", 0f, bounds.height)

        mgr.forAll[Renderable] { (e, r) =>
          val img = r.img
          for (p <- mgr.get[Position](e)) {
            val o = mgr.get[Orientation](e).getOrElse(Orientation(0))

            //                g2.drawString(s"(${Math.round(p.x)}, ${Math.round(p.y)})", p.x.asInstanceOf[Float], p.y.asInstanceOf[Float])

            val tx = new AffineTransform()
            tx.setToIdentity()
            tx.translate(p.x - r.width * 0.5, p.y - r.height * 0.5)
            tx.scale(r.scaleX, r.scaleY)
            tx.rotate(o.theta, img.getWidth * 0.5, img.getHeight * 0.5)

            g2.drawImage(img, tx, null)

            log.trace(s"Rendered ${mgr.getNickname(e).getOrElse("")} at $p with $o")
          }
        }
      }
    }

    paintHandler.doPaint(painter)
  }
}
