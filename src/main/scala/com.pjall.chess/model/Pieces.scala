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

class PointsByDirection(val directions: List[List[MoveToPoint]]) {

  def getAllPoints:List[MoveToPoint] = directions.flatten
  def +(points: List[MoveToPoint]): PointsByDirection = PointsByDirection(directions.concat(List(points)))
  def concat(newDirection:PointsByDirection) = PointsByDirection(directions.concat(newDirection.directions))

}

object PointsByDirection {
  def apply(directions: List[List[MoveToPoint]] = List.empty): PointsByDirection = new PointsByDirection(directions)
}

case class BasicPoint(x:Int, y:Int) extends Point

object PointBuilder {
  def apply(x: Int, y: Int): PointBuilder = new PointBuilder(x, y)
  def apply(point:Point): PointBuilder = new PointBuilder(point.x, point.y)

  def apply(x: Char, y: Int): PointBuilder = PointBuilder(x - 65, y)
}

case class MoveToPoint(point: Point, condition:Option[Piece] => Boolean)
case class PossibleMovement(moveFrom: Point,moveTo: Point)

object MoveToPoint {
  def apply(point: Point, condition:Option[Piece] => Boolean): MoveToPoint = new MoveToPoint(point, condition)
}

trait PointMovement {
  def whites(): Boolean

  def canMoveTo(point: Point):PointsByDirection
}

abstract case class Piece(override val whites:Boolean) extends PointMovement {
  val txt = "P"
  val isEmptyOrOtherTeam: Option[Piece] => Boolean = (piece:Option[Piece]) => piece.isEmpty || piece.get.whites != whites
  override def toString:String = if (whites) txt else txt.toLowerCase()

  def getPointsTillLimit(point: Point, getPoint: Point => PointBuilder, limit:Int):List[Point] = {
    var newPoint:Option[Point] = Some(point)
    var result: List[Point] = List()
    do {
      newPoint = getPoint(newPoint.get).build()
      result = newPoint.map(p => { result.concat(List(p)) }).getOrElse(result)
    } while (newPoint.isDefined && result.length < limit)
    result
  }

  def moveHorizontallyAndVertically(point: Point, limit:Int = 8): PointsByDirection = {
    var pointsByDirection = PointsByDirection()
    pointsByDirection = pointsByDirection.+(getPointsTillLimit(point, (point:Point) => PointBuilder(point).oneForward(whites), limit)
      .map(MoveToPoint(_, isEmptyOrOtherTeam)))
    pointsByDirection = pointsByDirection.+(getPointsTillLimit(point, (point:Point) => PointBuilder(point).oneBack(whites), limit)
      .map(MoveToPoint(_, isEmptyOrOtherTeam)))
    pointsByDirection = pointsByDirection.+(getPointsTillLimit(point, (point:Point) => PointBuilder(point).oneLeft(whites), limit)
      .map(MoveToPoint(_, isEmptyOrOtherTeam)))
    pointsByDirection.+(getPointsTillLimit(point, (point:Point) => PointBuilder(point).oneRight(whites), limit)
      .map(MoveToPoint(_, isEmptyOrOtherTeam)))
  }

  def moveDiagonally(point: Point, limit:Int = 8): PointsByDirection = {
    var pointsByDirection = PointsByDirection()
    pointsByDirection = pointsByDirection.+(getPointsTillLimit(point, (point:Point) => PointBuilder(point).oneForward(whites).oneRight(whites), limit)
      .map(MoveToPoint(_, isEmptyOrOtherTeam)))
    pointsByDirection = pointsByDirection.+(getPointsTillLimit(point, (point:Point) => PointBuilder(point).oneForward(whites).oneLeft(whites), limit)
      .map(MoveToPoint(_, isEmptyOrOtherTeam)))
    pointsByDirection = pointsByDirection.+(getPointsTillLimit(point, (point:Point) => PointBuilder(point).oneBack(whites).oneRight(whites), limit)
      .map(MoveToPoint(_, isEmptyOrOtherTeam)))
    pointsByDirection.+(getPointsTillLimit(point, (point:Point) => PointBuilder(point).oneBack(whites).oneLeft(whites), limit)
      .map(MoveToPoint(_, isEmptyOrOtherTeam)))
  }

}

class Pawn(whites:Boolean) extends Piece(whites) {
  val value = 1
  val isEmpty: Option[Piece] => Boolean = (piece:Option[Piece]) => piece.isEmpty

  override def canMoveTo(point: Point): PointsByDirection = {
    val thereIsAnEnemyPiece = (piece: Option[Piece]) => piece.isDefined && piece.get.whites != whites

    var result = PointsByDirection()
    val oneStep = PointBuilder(point.x, point.y).oneForward(whites).build().
      map(p => List(MoveToPoint(p, isEmpty))).getOrElse(List.empty)
    if ((point.y == 1 && whites) || (point.y == 6 && !whites)) {
      result = result.+(oneStep
        .concat(PointBuilder(point.x, point.y).oneForward(whites).oneForward(whites).build()
          .map(p => List(MoveToPoint(p, isEmpty))).getOrElse(List.empty)))
    } else {
      result = result.+(oneStep)
    }
    result = result.+(PointBuilder(point.x, point.y).oneForward(whites).oneLeft(whites).build().
      map(p => List(MoveToPoint(p, thereIsAnEnemyPiece))).getOrElse(List.empty))
    result = result.+(PointBuilder(point.x, point.y).oneForward(whites).oneRight(whites).build().
      map(p => List(MoveToPoint(p, thereIsAnEnemyPiece))).getOrElse(List.empty))

    result
  }

}

class Tower(whites:Boolean) extends Piece(whites) {
  val value = 6
  override val txt: String = "T"

  override def canMoveTo(point: Point):PointsByDirection = moveHorizontallyAndVertically(point)

}

class Horse(whites:Boolean) extends Piece(whites) {
  val value = 3
  override val txt: String = "H"

  override def canMoveTo(point: Point): PointsByDirection = {

    val lastMoveHorizontal = (pointBuilder:PointBuilder) => pointBuilder.oneRight(whites).build()
      .map( p => List(MoveToPoint(p, isEmptyOrOtherTeam))).getOrElse(List.empty)
      .concat(pointBuilder.oneLeft(whites).build()
        .map( p => List(MoveToPoint(p, isEmptyOrOtherTeam))).getOrElse(List.empty))

    val lastMoveVertical = (pointBuilder:PointBuilder) => pointBuilder.oneForward(whites).build()
      .map( p => List(MoveToPoint(p, isEmptyOrOtherTeam))).getOrElse(List.empty)
      .concat(pointBuilder.oneBack(whites).build()
        .map( p => List(MoveToPoint(p, isEmptyOrOtherTeam))).getOrElse(List.empty))

    val threeForward = PointBuilder(point.x, point.y).oneForward(whites).oneForward(whites)
    val threeBack = PointBuilder(point.x, point.y).oneBack(whites).oneBack(whites)
    val threeRight = PointBuilder(point.x, point.y).oneRight(whites).oneRight(whites)
    val threeLeft = PointBuilder(point.x, point.y).oneLeft(whites).oneLeft(whites)

    implicit def any2iterable[A](a: A) : Iterable[A] = Some(a)

    var pointsByDirection = PointsByDirection()
    lastMoveHorizontal(threeForward)
      .concat(lastMoveHorizontal(threeBack))
      .concat(lastMoveVertical(threeRight))
      .concat(lastMoveVertical(threeLeft)).flatten.foreach(p => pointsByDirection = pointsByDirection.+(List(p)))
    pointsByDirection
  }

}

class Bishop(whites:Boolean) extends Piece(whites) {
  val value = 3
  override val txt: String = "B"

  override def canMoveTo(point: Point): PointsByDirection = moveDiagonally(point)

}

class Queen(whites:Boolean) extends Piece(whites) {
  val value = 9
  override val txt: String = "Q"

  override def canMoveTo(point: Point): PointsByDirection =
    moveHorizontallyAndVertically(point).concat(moveDiagonally(point))
}

class King(whites:Boolean) extends Piece(whites) {
  val value = 0
  override val txt: String = "K"

  override def canMoveTo(point: Point): PointsByDirection =
    moveHorizontallyAndVertically(point, 1).concat(moveDiagonally(point, 1))

}

object Queen { def apply(whites:Boolean):Queen = new Queen(whites) }
object Pawn { def apply(whites:Boolean):Pawn = new Pawn(whites) }
object Horse { def apply(whites:Boolean):Horse = new Horse(whites) }
object Tower { def apply(whites:Boolean):Tower = new Tower(whites) }
object Bishop { def apply(whites:Boolean):Bishop = new Bishop(whites) }
object King { def apply(whites:Boolean):King = new King(whites) }



