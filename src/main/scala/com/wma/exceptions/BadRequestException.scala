package com.wma.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
case class BadRequestException(message: String = "Bad Request") extends RuntimeException{
  override def getMessage: String = message
}
