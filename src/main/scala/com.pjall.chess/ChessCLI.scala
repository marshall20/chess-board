

import com.pjall.chess.service.chess.BasicChessService

class ChessCLI {

  val chessService = new BasicChessService
  var game = chessService.newGame()
  val end = "end"
  val back = "back"
  val restart = "r"
  val possibilities = "p"
  start()

  def start(): Unit = {
    println("GL")
  }

}
