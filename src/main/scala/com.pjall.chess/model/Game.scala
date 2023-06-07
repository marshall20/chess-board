package com.pjall.chess.model

import scala.language.implicitConversions

case class Game(board: Map[Point, Piece], turn: Turn)

case class Turn(whites:Boolean = true) {

  def next: Turn = Turn(!whites)
  def whitesTurn: Boolean = whites

}



