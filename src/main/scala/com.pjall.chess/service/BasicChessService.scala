package com.pjall.chess.service

import com.pjall.chess.dto.Error.WrongPieceSelectedErrorCode
import com.pjall.chess.dto.{ErrorResponse, GenericResponse}
import com.pjall.chess.model.{Piece, Point}

class BasicChessService extends ChessService {

  val boardService = new ChessBoardService

  def start(): Unit = {
    boardService.newGame
    boardService.toString
  }

  override def selectPieceToMove(point: Point):GenericResponse[Piece]  =
    boardService.getPiece(point).filter(_.whites == turn.whitesTurn)
      .map(GenericResponse(_))
      .getOrElse(ErrorResponse(WrongPieceSelectedErrorCode()))

  override def retrieveMovePossibilities(point: Point): GenericResponse[Array[Point]] = ???

  override def move(toMove: Point, moveTo: Point): GenericResponse[Unit] = ???
}
