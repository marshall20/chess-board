package com.pjall.chess.movement

import com.pjall.chess.model._
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class TowerTest extends AnyFlatSpec {

  "A tower" should "move restrictions " in {
    val tower = Tower(true)
    val points = tower.canMoveTo(BasicPoint(4,4))
    assert(points.length == 14)
  }

  "A tower" should "move restrictions - crash" in {
    val tower = Tower(true)
    val points = tower.canMoveTo(BasicPoint(0,0))
    assert(points.length == 14)
  }

  "A tower" should "Pieces crash" in {
    val tower = Tower(true)
    val points = tower.canMoveTo(BasicPoint(0,0))
    val board: mutable.Map[Point, Piece] = mutable.Map[Point, Piece]()
    board.addOne(PointBuilder(0,5).build().get -> Pawn(false))
    board.addOne(PointBuilder(0,7).build().get -> Pawn(true))
    val filtered = points.filter(p => p.condition(board.get(p.point)))
    assert(filtered.length == 13)
  }



}