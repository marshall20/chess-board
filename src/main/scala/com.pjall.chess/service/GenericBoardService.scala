package com.pjall.chess.service

import com.pjall.chess.model.{Piece, Point, PointBuilder}

abstract class GenericBoardService extends BoardService {

  val BOARD_SIZE = 8

  override def getPiece(board: Map[Point, Piece], point: Point): Option[Piece] = board.get(point)

  override def movePiece(board: Map[Point, Piece], moveFrom: Point, moveTo: Point): Option[Map[Point, Piece]] =
    getPiece(board, moveFrom).map(p =>
      board.removed(moveFrom) ++ Map(moveTo -> p))


  override def print(board: Map[Point, Piece]): String = {
    var boardText = printableBoard()
    for (y <- 0 until board.size;
         x <- 0 until board.size) {
      boardText = boardText.replaceFirst("X", board.getOrElse(PointBuilder(x, y).build().get, "_").toString)
    }
    boardText
  }

  def printableBoard(): String = "      A   B   C   D   E   G   H   I   \n" +
    "     --- --- --- --- --- --- --- ---  \n" +
    " 0 |  X   X   X   X   X   X   X   X  |\n" +
    " 1 |  X   X   X   X   X   X   X   X  |\n" +
    " 2 |  X   X   X   X   X   X   X   X  |\n" +
    " 3 |  X   X   X   X   X   X   X   X  |\n" +
    " 4 |  X   X   X   X   X   X   X   X  |\n" +
    " 5 |  X   X   X   X   X   X   X   X  |\n" +
    " 6 |  X   X   X   X   X   X   X   X  |\n" +
    " 7 |  X   X   X   X   X   X   X   X  |\n" +
    "     --- --- --- --- --- --- --- ---  \n"
}
