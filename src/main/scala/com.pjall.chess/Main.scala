package com.pjall.chess

import service.BasicChessService

object Main extends App {

  val chessInterface = new BasicChessService
  chessInterface.start()

}