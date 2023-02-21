import scala.math.{ceil, sqrt}

val fun = (x:Double) => {
  x*3
}

def consumesFunction(fun : Double => Double, x:Double):Double = fun(x)

def producesFunction(x:Double) = fun(x)

1 to 10 map(consumesFunction(fun, _))
1 to 10 map(consumesFunction(ceil, _))
1 to 10 map(consumesFunction(sqrt, _))

1 to 10 map(producesFunction(_))

val zones = java.util.TimeZone.getAvailableIDs

zones.map(_.split("/")).filter(_.length > 1).map(_(1)).grouped(10).toArray.map(_(0))
1 to 3 reduceRightOption  (_ * _)
val x = Array("asd", "qwe", "xcv", "fgh")
x.reduceRight(_ + _)