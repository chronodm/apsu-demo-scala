package apsu.demo.rocks.components

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import org.apache.commons.math3.util.MathUtils

/**
 * Renderable
 *
 * @author david
 */
case class Renderable(width: Double, height: Double, imgResource: String) {
  lazy val img: BufferedImage = {
    val imgUrl = getClass.getResource(imgResource)
    ImageIO.read(imgUrl)
  }

  lazy val scaleX = width / img.getWidth
  lazy val scaleY = height / img.getHeight
}
