package apsu.demo.rocks.shared

import java.awt._
import java.awt.BufferCapabilities.FlipContents

/**
 * @author david
 */
class MainWindow(fullscreen: Boolean) extends PaintHandler {

  // ----------------------------------------
  // Fields

  lazy val frame = {
    val f = new Frame()
    f.setBackground(Color.black)
    f.setResizable(false)
    f.pack()
    f
  }

  lazy val bufferStrategy = {
    def imageCaps = new ImageCapabilities(true)
    frame.createBufferStrategy(2, new BufferCapabilities(imageCaps, imageCaps, FlipContents.BACKGROUND))
    frame.getBufferStrategy
  }

  // ----------------------------------------
  // Public methods

  def show() {
    if (fullscreen) {
      val env = GraphicsEnvironment.getLocalGraphicsEnvironment
      val dev = env.getDefaultScreenDevice
      dev.setFullScreenWindow(frame)

      // Keyboard focus hack; see http://stackoverflow.com/questions/13064607/fullscreen-swing-components-fail-to-receive-keyboard-input-on-java-7-on-mac-os-x
      frame.setVisible(false)
      frame.setVisible(true)
    } else {
      val screenSize = Toolkit.getDefaultToolkit.getScreenSize
      val scale  = 0.5f
      val frameSize = new Dimension(Math.round(scale * screenSize.width), Math.round(scale * screenSize.height))
      val frameX = (screenSize.width - frameSize.width) / 2
      val frameY = (screenSize.height - frameSize.height) / 2
      frame.setBounds(frameX, frameY, frameSize.width, frameSize.height)
      frame.setVisible(true)
    }
  }

  def getWidth = frame.getWidth

  def getHeight = frame.getHeight

  override def doPaint(painter: Painter): Unit = {
    val g2 = bufferStrategy.getDrawGraphics.asInstanceOf[Graphics2D]
    try {
      painter.paint(g2, frame.getBounds)
    } finally {
      g2.dispose()
    }
    bufferStrategy.show()
  }
}
