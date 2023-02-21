

import com.pjall.chess.model.{Piece, Point, PointBuilder}
import com.pjall.chess.service.BasicChessService

class ChessCLI {

  val chessInterface = new BasicChessService
  val end = "end"
  val back = "back"
  val restart = "r"
  val possibilities = "p"
  start()

  def start(): Unit = {
    println("GL")
    chessInterface.start()
    play()
  }

  def play(): Unit = {
    val input = scala.io.StdIn.readLine("Hello team, piece to move? Format E:5 r for restart end for end")
    if (input != end && input != restart) {
      extractInput(input).map(point => {
        chessInterface.selectPieceToMove(point) match {
          case piece: Piece => moveTo(piece)
          case _ => play()
        }
      })
    } else if (input == restart) {
      start()
    }
    println("GG")
  }

  def moveTo(piece: Piece): Unit = {
    val input = scala.io.StdIn.readLine("Hello team, move to? Format E:5, b to get back, p for possibilities")
    input match {
      case _: String if input == possibilities => displayPossibilities(piece)
      case _: String if input == back => play()
      case input: String =>
        extractInput(input).map(point => {
          chessInterface.selectPieceToMove(point) match {
            case piece: Piece => moveTo(piece)
            case _ => play()
          }
        })
      case _ => moveTo(piece)
    }
  }

  def displayPossibilities(piece: Piece): Unit = {
    chessInterface
  }

  def extractInput(userInput: String): Option[Point] = {
    val x = userInput.split(":")
    PointBuilder(x(0).last, x(1).toInt).build()
  }


}
