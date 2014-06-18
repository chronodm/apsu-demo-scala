package apsu.demo.rocks.shared

import java.awt.{Rectangle, Graphics2D}

/**
 * @version $Id$ $Rev$ $Date$
 */
trait Painter {
  def paint(g2: Graphics2D, bounds: Rectangle)
}

trait PaintHandler {
  def doPaint(painter: Painter)
}
