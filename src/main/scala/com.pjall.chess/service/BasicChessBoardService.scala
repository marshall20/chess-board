package com.pjall.chess.service

import com.pjall.chess.model.{Bishop, Horse, King, Pawn, Piece, Point, PointBuilder, Queen, Tower}

import scala.collection.mutable

class BasicChessBoardService extends GenericBoardService {

  override def newGame(board: mutable.Map[Point, Piece]): mutable.Map[Point, Piece] = {

    val whitesTeam = (y:Int) => if (y < 3) true else false

    val extraPieces = (x:Int, y:Int) =>
      x match {
        case _ if x == 3=> board.addOne(PointBuilder(x, y).build().get -> King(whitesTeam(y)))
        case _ if x == 4 => board.addOne(PointBuilder(x, y).build().get -> Queen(whitesTeam(y)))
        case _ if x == 0 || x ==7 =>  board.addOne(PointBuilder(x, y).build().get -> Tower(whitesTeam(y)))
        case _ if x == 1 || x ==6 =>  board.addOne(PointBuilder(x, y).build().get -> Horse(whitesTeam(y)))
        case _ if x == 2 || x ==5 =>  board.addOne(PointBuilder(x, y).build().get -> Bishop(whitesTeam(y)))
      }

    for (x <- 0 until BOARD_SIZE; y <- 0 until BOARD_SIZE) {
      y match {
        case y if y == 7 || y == 0 => extraPieces(x, y)
        case _ if y == 1 || y == 6 => board.addOne(PointBuilder(x, y).build().get -> Pawn(whitesTeam(y)))
        case _ =>
      }
    }
    board
  }



}
