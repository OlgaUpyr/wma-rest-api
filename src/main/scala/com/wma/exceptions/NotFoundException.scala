package com.wma.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
case class NotFoundException(message: String = "Not found") extends RuntimeException {
  override def getMessage: String = message
}
