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

  case class PieceCanNotBeMoved() extends Error {
    override def code: String = "PieceCanNotBeMoved"

    override def message: String = "This piece can not be moved"
  }

  case class ErrorMovingPieceAfterValidation() extends Error {
    override def code: String = "ErrorMovingPieceAfterValidation"

    override def message: String = "Though it was validated the piece could not be moved"
  }
  case class ErrorMovingPiece() extends Error {
    override def code: String = "ErrorMovingPiece"

    override def message: String = "The piece could not be moved"
  }



}

