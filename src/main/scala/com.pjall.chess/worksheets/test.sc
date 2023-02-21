class Time(h:Int, m:Int) {
  private val minutesSinceMidnight = h * 60 + m
  def hours = minutesSinceMidnight/ 60
  def minutes = minutesSinceMidnight % 60
  def this(hours:Int) = this(hours, 0)

  require(hours >= 0 && hours < 24, "Oops hours")
  require(minutes >= 0 && minutes < 59, "Oops minutes")

  override def toString() = f"($hours:$minutes%02d)"
  def < (time:Time) = minutesSinceMidnight < time.minutesSinceMidnight
  def - (time:Time) = minutesSinceMidnight - time.minutesSinceMidnight
}

object Time {
  def apply(h:Int, m:Int) = new Time(h,m)
}

val x =new Time(10,5)
val y =new Time(10,51)
x < y
x - y

Time(10,9)