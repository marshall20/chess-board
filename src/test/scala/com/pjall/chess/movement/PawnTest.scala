package com.pjall.chess.movement

import com.pjall.chess.model.{BasicPoint, Pawn, Piece, Point, PointBuilder}
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class PawnTest extends AnyFlatSpec {

  "A Pawn" should "move restrictions crash left" in {
    val pawn = Pawn(true)
    val points = pawn.canMoveTo(BasicPoint(0,1)).getAllPoints
    assert(points.length == 3)
    val filtered = points.filter(_.condition(None))
    assert(filtered.length == 2)
  }
  "A Pawn" should "move restrictions crash right" in {
    val pawn = Pawn(true)
    val points = pawn.canMoveTo(BasicPoint(7,1)).getAllPoints
    assert(points.length == 3)
    val filtered = points.filter(_.condition(None))
    assert(filtered.length == 2)
  }

  "A Pawn" should "move restrictions" in {
    val pawn = Pawn(true)
    val points = pawn.canMoveTo(BasicPoint(3,1)).getAllPoints
    assert(points.length == 4)
    val filtered = points.filter(_.condition(None))
    assert(filtered.length == 2)
  }

  "A Pawn" should "move restrictions attack one other team another same team" in {
    val p = Pawn(true)
    val points = p.canMoveTo(BasicPoint(3,1)).getAllPoints
    assert(points.length == 4)
    val board: mutable.Map[Point, Piece] = mutable.Map[Point, Piece]()
    board.addOne(PointBuilder(2,2).build().get -> Pawn(false))
    board.addOne(PointBuilder(4,2).build().get -> Pawn(true))

    val filtered = points.filter(p => p.condition(board.get(p.point)))

    assert(filtered.length == 3)
  }

}