package com.pjall.chess.dto

trait Error {
  def code: String
  def message: String
  override def toString: String = code
  override def equals(other: Any): Boolean = other match {
    case otherErrorCode: Error ⇒ otherErrorCode.code == code && otherErrorCode.message == message
    case _                         ⇒ false
  }
}

object Error {

  case class WrongPieceSelectedErrorCode() extends Error {
    override def code: String = "WrongPieceSelected"

    override def message: String = "This is not a valid piece for selection"
  }


}

