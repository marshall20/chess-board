package com.pjall.chess.service

import com.pjall.chess.model.{Piece, Point, PointBuilder}

import scala.collection.mutable

abstract class BoardService {

  protected val board: mutable.Map[Point, Piece] = mutable.Map[Point, Piece]()
  val BOARD_SIZE = 8

  def newGame(): Unit = { }

  def getPiece(point: Point):Option[Piece] = {
    board.get(point)
  }

  def getBoard() = board.clone()

  def movePiece(moveFrom: Point, moveTo: Point): Boolean = getPiece(moveFrom).exists(p => {
    board.remove(moveFrom)
    board.addOne(moveTo -> p)
    true
  })

  override def toString: String = {
    var boardText = printableBoard()
    for (y <- 0 until BOARD_SIZE; x <- 0 until BOARD_SIZE) {
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
                                 "   --- --- --- --- --- --- --- ---  \n"
}
