package com.pjall.chess.service

import com.pjall.chess.model.{Piece, Point}

trait BoardService {

  def getPiece(board: Map[Point, Piece], point: Point): Option[Piece]
  def movePiece(board: Map[Point, Piece], moveFrom: Point, moveTo: Point): Option[Map[Point, Piece]]
  def print(board: Map[Point, Piece]): String
  def newBoard(): Map[Point, Piece]

}
