trait RectangleLike {
  def setFrame(x:Double, y:Double, w:Double, h:Double)
  def getWidth():Double
  def getHeight():Double
  def getX():Double
  def getY():Double
  def translate(dx:Double, dy:Double): Unit = {
    setFrame(getX()+ dx, getY()+dy, getWidth(), getHeight())
  }
  override def toString() = f"$getX, $getY, $getWidth, $getHeight"
}

import java.awt.geom.Ellipse2D
val ellipse2D = new Ellipse2D.Double(10, 10, 10, 10) with RectangleLike
ellipse2D.translate(20, 30)
ellipse2D.toString