package apsu.demo.rocks.components

import java.awt.Image
import javax.imageio.ImageIO

/**
 * Renderable
 *
 * @author david
 */
case class Renderable(width: Double, height: Double, imgResource: String) {
  lazy val img: Image = {
    val imgUrl = getClass.getResource(imgResource)
    ImageIO.read(imgUrl)
  }
}
