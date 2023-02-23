package com.pjall.chess.service

import com.pjall.chess.dto.Error.{ErrorMovingPiece, ErrorMovingPieceAfterValidation, WrongPieceSelectedErrorCode}
import com.pjall.chess.dto.{ErrorResponse, GenericResponse}
import com.pjall.chess.model.{King, MoveToPoint, Piece, Point, PointBuilder, PointsByDirection}

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

  override def retrieveMovePossibilities(point: Point): GenericResponse[List[Point]] =
    boardService.getPiece(point) match {
      case piece: Piece => GenericResponse(movementPossibilities(piece, point).map(_.point))
      case None => ErrorResponse(WrongPieceSelectedErrorCode())
    }

  def movementPossibilities(piece: Piece, point: Point): List[MoveToPoint] = {
    discardBlockedDirections(piece.canMoveTo(point)).
      filter(p => p.condition(boardService.getPiece(p.point)))
  }

  private def discardBlockedDirections(pointsByDirection: PointsByDirection):List[MoveToPoint] = {
    var points:List[MoveToPoint] = List.empty
      pointsByDirection.directions.foreach( direction => {
        var crash = false
        direction.foreach(pointInDirection => {
          if (!crash && boardService.getPiece(pointInDirection.point).isDefined) {
           crash = true
           points = points.concat(List(pointInDirection))
          }
        })
      })
    List.empty
  }

  override def move(toMove: Point, moveTo: Point): GenericResponse[Unit] = {
        retrieveMovePossibilities(toMove) match {
      case response if response.success && response.result.get.contains(moveTo) =>
        if (boardService.movePiece(toMove, moveTo)) {
          GenericResponse()
        } else {
          ErrorResponse(ErrorMovingPieceAfterValidation())
        }
      case _ =>
        ErrorResponse(ErrorMovingPiece())
    }
  }

  override def isInCheck(): GenericResponse[Boolean] = {
    for ((point, piece) <- boardService.getBoard().iterator) {
      val piecesThatCanAttackKing = movementPossibilities(piece, point).filter( p =>
        boardService.getPiece(p.point).isDefined && p.condition(Some(piece))
      )
      if (piecesThatCanAttackKing.size > 0) {
        return GenericResponse(true)
      }
    }
    GenericResponse(false)
  }

  override def checkMate(): GenericResponse[Boolean] = ???
}
