package apsu.demo.rocks.components.rendering

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Renderable
 *
 * @author david
 */
case class Renderable(width: Double, height: Double, imgResource: String) {
  val img: BufferedImage = {
    val imgUrl = getClass.getResource(imgResource)
    ImageIO.read(imgUrl)
  }

  val scaleX = width / img.getWidth
  val scaleY = height / img.getHeight
}
