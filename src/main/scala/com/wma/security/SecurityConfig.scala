package com.wma.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.{InterceptorRegistry, WebMvcConfigurerAdapter}

@Configuration
class SecurityConfig(@Value("${wma.cors.allowedOrigin}") allowedOrigin: String,
                     @Value("${wma.cors.allowCredentials}") allowedCredentials: String,
                     @Value("${wma.cors.location}") location: String
                    ) extends WebMvcConfigurerAdapter {

  override def addInterceptors(registry: InterceptorRegistry): Unit = {

    registry.addInterceptor(new CORSInterceptor("*", "false", "http://localhost:8080"))
  }
}

