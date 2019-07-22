package com.wma.security

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.{HandlerInterceptor, ModelAndView}


class CORSInterceptor(allowedOrigin: String, allowedCredentials: String, location: String) extends HandlerInterceptor {

  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = {
    if (request.getMethod.toUpperCase() == "OPTIONS") {
      response.setHeader("Access-Control-Allow-Origin", allowedOrigin)
      response.setHeader("Access-Control-Allow-Credentials", allowedCredentials)
      response.setHeader("Access-Control-Allow-Headers",s"Origin,X-Requested-With,Content-Type,Accept")
      response.setStatus(HttpStatus.OK.value())
      return false
    } else {
      response.setHeader("Access-Control-Allow-Origin", allowedOrigin)
      response.setHeader("Access-Control-Allow-Credentials", allowedCredentials)
      response.setHeader("Referrer-Policy", "origin-when-cross-origin")
      response.setHeader("X-Frame-Options", "sameorigin")
      response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
      response.setHeader("Location", location)
      response.setHeader("X-XSS-Protection", "1; mode=block")
      response.setHeader("Content-Security-Policy-Report-Only", "default-src 'self';")
    }
    true
  }

  override def afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, ex: Exception): Unit = {}

  override def postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any, modelAndView: ModelAndView): Unit = {}
}

