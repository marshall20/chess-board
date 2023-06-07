package com.pjall.chess.service.chess

import com.pjall.chess.model._

class BasicChessService() extends ChessService {

  override def newGame(lastTurnWhites: Boolean = false): Game = {
    Game(BasicChessBoardService.newBoard(), Turn(!lastTurnWhites))
  }

  override def selectPieceToMove(game: Game, point: Point): Option[Piece] = BasicChessBoardService.
    getPiece(game.board, point).filter(_.whites == game.turn.whitesTurn)


  override def getPossibleMoves(game: Game, point: Point): Either[String, List[Point]] =
    BasicChessBoardService.getPiece(game.board, point) match {
      case Some(piece) if piece.whites.equals(game.turn.whitesTurn) => Right(movementPossibilities(game, piece, point).map(_.point))
      case _ => Left("Not a valid piece to move")
    }

  override def move(game: Game, from: Point, to: Point): Either[String, Game] = {
    getPossibleMoves(game, from) match {
      case Right(moves) if moves.contains(to) =>
        BasicChessBoardService.movePiece(game.board, from, to) match {
          case Some(x) => Right(Game(x, game.turn.next))
          case None => Left("Failed to move piece to destination")
        }
      case Right(_) => Left("Not a valid destination")
      case Left(error) => Left(error)
    }
  }

  override def isInCheck(game: Game): Boolean = ???

  override def iCheckMate(game: Game): Boolean = ???

  def movementPossibilities(game: Game, piece: Piece, point: Point): List[MoveToPoint] = {
    discardBlockedDirections(game, piece.canMoveTo(point)).
      filter(p => p.condition(BasicChessBoardService.getPiece(game.board, p.point)))
  }

  private def discardBlockedDirections(game: Game, pointsByDirection: PointsByDirection): List[MoveToPoint] = {
    pointsByDirection.directions.map(direction => {

      def removeBlocked(moveToPoints: List[MoveToPoint]): List[MoveToPoint] = {
        moveToPoints.collectFirst(moveToPoint => {
          BasicChessBoardService.getPiece(game.board, moveToPoint.point) match {
            case Some(piece) if piece.whites.equals(game.turn.whites) => List.empty
            case Some(_) => List(moveToPoint)
            case None => List(moveToPoint) ++ removeBlocked(moveToPoints.tail)
          }
        }).getOrElse(List.empty)
      }
      removeBlocked(direction)
    }).flatten
  }

}
