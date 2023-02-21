package com.pjall.chess.model

import scala.util.Try

sealed trait Point {
  val BOARD_MAX = 8
  val BOARD_MIN = 0
  def x: Int
  def y: Int

  require(x >= BOARD_MIN && y >= BOARD_MIN && x < BOARD_MAX && y < BOARD_MAX, "Invalid dto.Point")
}

case class PointBuilder(x:Int, y:Int) {
  def oneForward(withes:Boolean): PointBuilder = if (withes) PointBuilder(x, y+1) else PointBuilder(x, y-1)
  def oneBack(withes:Boolean): PointBuilder = if (withes) PointBuilder(x, y-1) else PointBuilder(x, y-1)
  def oneRight(withes:Boolean): PointBuilder = if (withes) PointBuilder(x+1 , y) else PointBuilder(x-1, y)
  def oneLeft(withes:Boolean): PointBuilder = if (withes) PointBuilder(x-1 , y) else PointBuilder(x+1, y)

  def build():Option[Point] = Try {
      BasicPoint(x,y)
    }.toOption

}

case class BasicPoint(x:Int, y:Int) extends Point

object PointBuilder {
  def apply(x: Int, y: Int): PointBuilder = new PointBuilder(x, y)
  def apply(point:Point): PointBuilder = new PointBuilder(point.x, point.y)

  def apply(x: Char, y: Int): PointBuilder = PointBuilder(x - 65, y)
}

case class MoveToPoint(point: Point, condition:Option[Piece] => Boolean)

object MoveToPoint {
  def apply(point: Point, condition:Option[Piece] => Boolean): MoveToPoint = new MoveToPoint(point, condition)
}

trait Movement {
  val BOARD_MAX = 8
  val BOARD_MIN = 0
  def whites(): Boolean

  def canMoveTo(point: Point):List[MoveToPoint]
}

abstract case class Piece(override val whites:Boolean) extends Movement {
  val txt = "P"
  val isEmptyOrOtherTeam: Option[Piece] => Boolean = (piece:Option[Piece]) => piece.isEmpty || piece.get.whites != whites
  override def toString:String = if (whites) txt else txt.toLowerCase()

  def getPointsTillBoardLimit(point: Point, getPoint: Point => PointBuilder ):List[Point] = {
    var newPoint:Option[Point] = Some(point)
    var result: List[Point] = List()
    do {
      newPoint = getPoint(newPoint.get).build()
      result = newPoint.map(p => { result.concat(List(p)) }).getOrElse(result)
    } while (newPoint.isDefined)
    result
  }
}

class Pawn(whites:Boolean) extends Piece(whites) {
  val value = 1
  val isEmpty: Option[Piece] => Boolean = (piece:Option[Piece]) => piece.isEmpty


  override def canMoveTo(point: Point): List[MoveToPoint] = {
    val thereIsAnEnemyPiece = (piece:Option[Piece]) => piece.isDefined && piece.get.whites != whites

    val result =
      PointBuilder(point.x, point.y).oneForward(whites).oneLeft(whites).build().
        map(p => List(MoveToPoint(p, thereIsAnEnemyPiece))).getOrElse(List.empty).concat(
        PointBuilder(point.x, point.y).oneForward(whites).oneRight(whites).build().
          map(p => List(MoveToPoint(p, thereIsAnEnemyPiece))).getOrElse(List.empty)).concat(
        PointBuilder(point.x, point.y).oneForward(whites).build().
          map(p => List(MoveToPoint(p, isEmpty))).getOrElse(List.empty)
      )
    if ((point.y == 1 && whites) || (point.y == 6 && !whites)) {
      result.concat(PointBuilder(point.x, point.y).oneForward(whites).oneForward(whites).build()
        .map( p => List(MoveToPoint(p, isEmpty))).getOrElse(List.empty))
    } else {
      result
    }
  }

}

class Tower(whites:Boolean) extends Piece(whites) {
  val value = 6
  override val txt: String = "T"

  override def canMoveTo(point: Point): List[MoveToPoint] = {
    val front = getPointsTillBoardLimit(point, (point:Point) => PointBuilder(point).oneForward(whites))
    val back = getPointsTillBoardLimit(point, (point:Point) => PointBuilder(point).oneBack(whites))
    val left = getPointsTillBoardLimit(point, (point:Point) => PointBuilder(point).oneLeft(whites))
    val right = getPointsTillBoardLimit(point, (point:Point) => PointBuilder(point).oneRight(whites))
    front.concat(back).concat(left).concat(right).map(MoveToPoint(_, isEmptyOrOtherTeam))
  }

}

class Horse(whites:Boolean) extends Piece(whites) {
  val value = 3
  override val txt: String = "H"

  override def canMoveTo(point: Point): List[MoveToPoint] = ???

}

class Bishop(whites:Boolean) extends Piece(whites) {
  val value = 3
  override val txt: String = "B"

  override def canMoveTo(point: Point): List[MoveToPoint] = ???

}

class Queen(whites:Boolean) extends Piece(whites) {
  val value = 9
  override val txt: String = "Q"

  override def canMoveTo(point: Point): List[MoveToPoint] = ???

}

class King(whites:Boolean) extends Piece(whites) {
  val value = 0
  override val txt: String = "K"

  override def canMoveTo(point: Point): List[MoveToPoint] = ???

}
  object Queen { def apply(whites:Boolean):Queen = new Queen(whites) }
object Pawn { def apply(whites:Boolean):Pawn = new Pawn(whites) }
object Horse { def apply(whites:Boolean):Horse = new Horse(whites) }
object Tower { def apply(whites:Boolean):Tower = new Tower(whites) }
object Bishop { def apply(whites:Boolean):Bishop = new Bishop(whites) }
object King { def apply(whites:Boolean):King = new King(whites) }



