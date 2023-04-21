package com.pjall.chess.service

import com.pjall.chess.model.{Piece, Point, PointBuilder}

import scala.collection.mutable

abstract class GenericBoardService extends BoardService {

  val BOARD_SIZE = 8

  override def getPiece(board: mutable.Map[Point, Piece], point: Point):Option[Piece] = {
    board.get(point)
  }

  override def movePiece(board: mutable.Map[Point, Piece], moveFrom: Point, moveTo: Point): Boolean = getPiece(board, moveFrom).exists(p => {
    board.remove(moveFrom)
    board.addOne(moveTo -> p)
    true
  })

  override def print(board: mutable.Map[Point, Piece]): String = {
    var boardText = printableBoard()
    for (y <- 0 until board.size; x <- 0 until board.size) {
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
