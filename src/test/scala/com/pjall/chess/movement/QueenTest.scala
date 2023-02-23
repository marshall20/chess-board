package com.pjall.chess.movement

import com.pjall.chess.model._
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class QueenTest extends AnyFlatSpec {

  "A Queen" should "move restrictions " in {
    val queen = Queen(true)
    val points = queen.canMoveTo(BasicPoint(4,4)).getAllPoints
    assert(points.length == 13 + 14)
  }

  "A Queen" should "move restrictions - crash" in {
    val queen = Queen(true)
    val points = queen.canMoveTo(BasicPoint(0,0)).getAllPoints
    assert(points.length == 7 + 14)
  }

  "A Queen" should "move restrictions - crash 2" in {
    val queen = Queen(true)
    val points = queen.canMoveTo(BasicPoint(1,2)).getAllPoints
    assert(points.length == 9 + 14)
  }

  "A Queen" should "Pieces crash" in {
    val queen = Queen(true)
    val points = queen.canMoveTo(BasicPoint(1,2)).getAllPoints
    assert(points.length == 9 + 14)
    val board: mutable.Map[Point, Piece] = mutable.Map[Point, Piece]()
    board.addOne(PointBuilder(4,5).build().get -> Pawn(false))
    board.addOne(PointBuilder(6,7).build().get -> Pawn(true))
    val filtered = points.filter(p => p.condition(board.get(p.point)))
    assert(filtered.length == 8 + 14)

  }



}