package com.pjall.chess.service

import com.pjall.chess.model.{Piece, Point}
import com.pjall.chess.dto.GenericResponse
import com.pjall.chess.util.Turn

trait ChessService {

  val turn:Turn = Turn()

  def selectPieceToMove(point: Point):GenericResponse[Piece]

  def retrieveMovePossibilities(point: Point): GenericResponse[Array[Point]]

  def move(toMove: Point, moveTo: Point):GenericResponse[Unit]

}
