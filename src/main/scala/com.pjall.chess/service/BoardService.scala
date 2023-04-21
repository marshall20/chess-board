package com.pjall.chess.service

import com.pjall.chess.model.{Piece, Point}

import scala.collection.mutable

trait BoardService {

  def getPiece(board: mutable.Map[Point, Piece], point: Point):Option[Piece]
  def movePiece(board: mutable.Map[Point, Piece], moveFrom: Point, moveTo: Point): Boolean
  def print(board: mutable.Map[Point, Piece]): String
  def newGame(board: mutable.Map[Point, Piece]): mutable.Map[Point, Piece]

}
