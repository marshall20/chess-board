package com.pjall.chess.service.chess

import com.pjall.chess.model.{Game, Piece, Point}

trait ChessService {

  def newGame(lastTurnWhites: Boolean = false): Game

  def selectPieceToMove(game: Game, point: Point): Option[Piece]

  def getPossibleMoves(game: Game, point: Point): Either[String, List[Point]]

  def move(game: Game, toMove: Point, moveTo: Point): Either[String, Game]

  def isInCheck(game: Game): Boolean

  def iCheckMate(game: Game): Boolean

}
