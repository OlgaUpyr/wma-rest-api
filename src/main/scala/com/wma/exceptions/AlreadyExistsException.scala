package com.wma.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
case class AlreadyExistsException(message: String = "Already exists") extends RuntimeException {
  override def getMessage: String = message
}
