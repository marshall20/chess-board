package com.pjall.chess.util

trait Turn {

  def next:Unit
  def whitesTurn:Boolean
}

object Turn {
  def apply(): Turn = new BasicTurn(true)

  private class BasicTurn(val turn: Boolean) extends Turn {
    private var whites = turn
    override def next: Unit = whites = !whites
    override def whitesTurn: Boolean = whites
  }
}
