package apsu.demo.rocks.components.rendering

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Renderable
 *
 * @author david
 */
case class Renderable(width: Float, height: Float, imgResource: String) {
  val img: BufferedImage = {
    val imgUrl = getClass.getResource(imgResource)
    ImageIO.read(imgUrl)
  }

  val scaleX = width / img.getWidth
  val scaleY = height / img.getHeight

  val radius = (width + height) * 0.25 // average of 1/2 width & 1/2 height
}
