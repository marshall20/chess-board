package com.pjall.chess.dto

case class GenericResponse[+A](
                                     success: Boolean,
                                     errorMessage: Option[String],
                                     errorCode: Option[String],
                                     result: Option[A]) extends Response { }

object GenericResponse {
  def apply[A](result: A): GenericResponse[A] = GenericResponse(true, None, None, Some(result))

}

object ErrorResponse {
  def apply[A](error: Error): GenericResponse[A] = GenericResponse(false, Some(error.message), Some(error.code), None)
}

sealed trait Response {
  def success: Boolean
  def errorCode: Option[String]

}
