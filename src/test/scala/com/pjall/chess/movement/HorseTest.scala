package com.pjall.chess.movement

import com.pjall.chess.model._
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class HorseTest extends AnyFlatSpec {

  "A horse" should "move restrictions " in {
    val horse = Horse(true)
    val points = horse.canMoveTo(BasicPoint(4,4)).getAllPoints
    assert(points.length == 8)
  }

  "A horse" should "move restrictions - crash" in {
    val horse = Horse(true)
    val points = horse.canMoveTo(BasicPoint(0,0)).getAllPoints
    assert(points.length == 2)
  }

  "A horse" should "move restrictions - crash 2" in {
    val horse = Horse(true)
    val points = horse.canMoveTo(BasicPoint(1,2)).getAllPoints
    assert(points.length == 6)
  }

  "A horse" should "Pieces crash" in {
    val horse = Horse(true)
    val points = horse.canMoveTo(BasicPoint(1,2)).getAllPoints
    assert(points.length == 6)
    val board: mutable.Map[Point, Piece] = mutable.Map[Point, Piece]()
    board.addOne(PointBuilder(3,3).build().get -> Pawn(false))
    board.addOne(PointBuilder(2,4).build().get -> Pawn(true))
    val filtered = points.filter(p => p.condition(board.get(p.point)))
    assert(filtered.length == 5)

  }



}