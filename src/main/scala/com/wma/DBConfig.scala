package com.wma

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.jdbc.datasource.DriverManagerDataSource

@Configuration
class DBConfig {

  @Bean
  def dataSource: DriverManagerDataSource = {
    val dataSource = new DriverManagerDataSource
    dataSource.setDriverClassName("org.postgresql.Driver")
    dataSource.setUrl("jdbc:postgresql://localhost:5432/wma")
    dataSource.setUsername("postgres")
    dataSource.setPassword("postgres_password")
    dataSource
  }
}