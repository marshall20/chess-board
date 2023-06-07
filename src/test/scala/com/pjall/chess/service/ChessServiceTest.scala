package com.pjall.chess.service

import com.pjall.chess.model._
import com.pjall.chess.service.chess.BasicChessService
import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ChessServiceTest extends AnyFlatSpec with Inside with Matchers {

  val chessService = new BasicChessService
  val game = chessService.newGame()

  "new game" should "should create a new Board where whites start" in {
    game.board.foreach(pair => {
      pair._1 match {
        case p: Point if p.y == 1 || p.y == 6 => assert(pair._2.isType[Pawn])
        case p: Point if (p.y == 0 || p.y == 7) && (p.x == 0 || p.x == 7) => assert(pair._2.isType[Tower])
        case p: Point if (p.y == 0 || p.y == 7) && (p.x == 1 || p.x == 6) => assert(pair._2.isType[Horse])
        case p: Point if (p.y == 0 || p.y == 7) && (p.x == 2 || p.x == 5) => assert(pair._2.isType[Bishop])
        case p: Point if (p.y == 0 && p.x == 3) || (p.y == 7 || p.x == 4) => assert(pair._2.isType[King])
        case p: Point if (p.y == 0 && p.x == 4) || (p.y == 7 || p.x == 3) => assert(pair._2.isType[Queen])
        case _ => assert(false)
      }
    })
    assert(game.turn.whites)
  }

  "new game" should "should create a new Board where blacks start" in {
    val blacksGames = chessService.newGame(true)
    assert(!blacksGames.turn.whites)
  }

  "select piece" should "select piece from your team" in {
    val pieceOpt = chessService.selectPieceToMove(game, BasicPoint(0, 1))
    assert(pieceOpt.get.isType[Pawn])
  }

  "select piece" should "fails to select piece from other team" in {
    val pieceOpt = chessService.selectPieceToMove(game, BasicPoint(0, 6))
    assert(pieceOpt.isEmpty)
  }

  "possibilities to move" should "Bishop diagonal moves" in {
    val whitesPawn = PointBuilder(3, 1)
    val blackPawn = PointBuilder(7, 6)
    val whiteBishop = PointBuilder(2,0)

    var updatedGame = chessService.move(game, whitesPawn.buildPoint(), whitesPawn.oneForward(true).oneForward(true).buildPoint()) match {
      case Right(updatedGame) => updatedGame
      case Left(_) => game
    }
    assert(!game.equals(updatedGame), "Map not updated")

    updatedGame = chessService.move(updatedGame, blackPawn.buildPoint(), blackPawn.oneForward(false).buildPoint()) match {
      case Right(updatedGame) => updatedGame
      case Left(_) => game
    }

    assert(!game.equals(updatedGame), "Map not updated")

    val moves = chessService.getPossibleMoves(updatedGame,whiteBishop.buildPoint()) match {
      case Right(moves) => moves
      case Left(_) => List.empty
    }
    assert(moves.size == 5, "Insufficient moves")
  }

  "Move horse" should "Horse moves" in {
    val whitesPawn = PointBuilder(0, 2)
    val blackPawn = PointBuilder(2, 2)
    val whiteHorse = PointBuilder(1,0)
    val game = Game(Map(whitesPawn.buildPoint() -> Pawn(true)) ++
      Map(blackPawn.buildPoint() -> Pawn(false)) ++
      Map(whiteHorse.buildPoint() -> Horse(true)), Turn())

    val moves = chessService.getPossibleMoves(game,whiteHorse.buildPoint()) match {
      case Right(moves) => moves
      case Left(_) => List.empty
    }
    assert(moves.size == 2, "Wrong number of moves")

    var updatedGame = chessService.move(game, whiteHorse.buildPoint(), whitesPawn.buildPoint()) match {
      case Right(updatedGame) => updatedGame
      case Left(_) => game
    }

    assert(game.equals(updatedGame), "Map updated incorrectly")
    assert(updatedGame.board.size == 3, "Piece removed incorrectly")

    updatedGame = chessService.move(game, whiteHorse.buildPoint(), blackPawn.buildPoint()) match {
      case Right(updatedGame) => updatedGame
      case Left(_) => game
    }
    assert(!game.equals(updatedGame), "Map not updated")

    assert(updatedGame.board.size == 2, "Piece removed correctly")

  }

  "Not Move queen" should "do not move queen to blocked directions" in {
    val whitesPawn = PointBuilder(4, 5)
    val blackPawn = PointBuilder(5, 5)
    val whiteQueen = PointBuilder(4,4)
    val game = Game(Map(whitesPawn.buildPoint() -> Pawn(true)) ++
      Map(blackPawn.buildPoint() -> Pawn(false)) ++
      Map(whiteQueen.buildPoint() -> Queen(true)), Turn())

    val moves = chessService.getPossibleMoves(game,whiteQueen.buildPoint()) match {
      case Right(moves) => moves
      case Left(_) => List.empty
    }
    assert(moves.size == 22, "Wrong number of moves")

    var updatedGame = chessService.move(game, whiteQueen.buildPoint(), whitesPawn.oneForward(true).buildPoint()) match {
      case Right(updatedGame) => updatedGame
      case Left(_) => game
    }

    assert(game.equals(updatedGame), "Map updated incorrectly")
    assert(updatedGame.board.size == 3, "Piece removed incorrectly")

    updatedGame = chessService.move(game, whiteQueen.buildPoint(), blackPawn.oneBack(false).oneLeft(false).buildPoint()) match {
      case Right(updatedGame) => updatedGame
      case Left(_) => game
    }

    assert(game.equals(updatedGame), "Map updated incorrectly")

    assert(updatedGame.board.size == 3, "Incorrect number of pieces")

  }

}