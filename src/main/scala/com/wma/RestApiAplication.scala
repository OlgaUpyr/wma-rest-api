package com.wma

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class RestApiApplication

object RestApiApplication extends App {

  SpringApplication.run(classOf[RestApiApplication])

  def appPackage: String = classOf[RestApiApplication].getPackage.getName
}
