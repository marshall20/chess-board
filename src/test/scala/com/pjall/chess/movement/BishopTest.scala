package com.pjall.chess.movement

import com.pjall.chess.model._
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class BishopTest extends AnyFlatSpec {

  "A bishop" should "move restrictions " in {
    val bishop = Bishop(true)
    val points = bishop.canMoveTo(BasicPoint(4,4)).getAllPoints
    assert(points.length == 13)
  }

  "A bishop" should "move restrictions - crash" in {
    val bishop = Bishop(true)
    val points = bishop.canMoveTo(BasicPoint(0,0)).getAllPoints
    assert(points.length == 7)
  }

  "A bishop" should "move restrictions - crash 2" in {
    val bishop = Bishop(true)
    val points = bishop.canMoveTo(BasicPoint(1,2)).getAllPoints
    assert(points.length == 9)
  }

  "A bishop" should "Pieces crash" in {
    val bishop = Bishop(true)
    val points = bishop.canMoveTo(BasicPoint(1,2)).getAllPoints
    assert(points.length == 9)
    val board: mutable.Map[Point, Piece] = mutable.Map[Point, Piece]()
    board.addOne(PointBuilder(4,5).build().get -> Pawn(false))
    board.addOne(PointBuilder(6,7).build().get -> Pawn(true))
    val filtered = points.filter(p => p.condition(board.get(p.point)))
    assert(filtered.length == 8)

  }



}