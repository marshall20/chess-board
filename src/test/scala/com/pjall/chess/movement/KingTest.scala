package com.pjall.chess.movement

import com.pjall.chess.model._
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class KingTest extends AnyFlatSpec {

  "A king" should "move restrictions " in {
    val king = King(true)
    val points = king.canMoveTo(BasicPoint(4,4)).getAllPoints
    assert(points.length == 8)
  }

  "A king" should "move restrictions - crash" in {
    val king = King(true)
    val points = king.canMoveTo(BasicPoint(0,0)).getAllPoints
    assert(points.length == 3)
  }

  "A king" should "move restrictions - crash 2" in {
    val king = King(true)
    val points = king.canMoveTo(BasicPoint(1,2)).getAllPoints
    assert(points.length == 8)
  }

  "A king" should "Pieces crash" in {
    val king = King(true)
    val points = king.canMoveTo(BasicPoint(1,2)).getAllPoints
    assert(points.length == 8)
    val board: mutable.Map[Point, Piece] = mutable.Map[Point, Piece]()
    board.addOne(PointBuilder(1,3).build().get -> Pawn(false))
    board.addOne(PointBuilder(0,1).build().get -> Pawn(true))
    val filtered = points.filter(p => p.condition(board.get(p.point)))
    assert(filtered.length == 7)

  }



}