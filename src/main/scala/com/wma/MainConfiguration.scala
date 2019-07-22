package com.wma

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import javax.sql.DataSource
import org.apache.http.impl.client.HttpClients
import org.springframework.context.annotation.{Bean, Configuration, Primary}
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartResolver
import org.springframework.web.multipart.support.StandardServletMultipartResolver
import play.api.libs.json.JsValue



@Configuration
class MainConfiguration(val dataSource: DataSource) {

  @Primary
  @Bean
  def beanPasswordEncoder(): BCryptPasswordEncoder = {
    new BCryptPasswordEncoder(7)
  }

  @Primary
  @Bean
  def beanObjectMapper(): ObjectMapper = {
    new ObjectMapper()
      .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
      .registerModule(new SimpleModule().addSerializer(classOf[JsValue], new WMAJsonObjectScala()))
      .registerModule(new Hibernate5Module())
      .registerModule(new JavaTimeModule())
  }

  @Bean
  def beanJacksonMessageConverter(objectMapper: ObjectMapper): MappingJackson2HttpMessageConverter = {
    new MappingJackson2HttpMessageConverter(objectMapper)
  }

  @Bean
  def multipartResolver(): MultipartResolver = {
    new StandardServletMultipartResolver()
  }

  @Bean
  def beanRestTemplate(): RestTemplate = {
    val timeout = 5000
    val defaultHttpClient = HttpClients
      .custom()
      .build()

    val clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(defaultHttpClient)
    clientHttpRequestFactory.setConnectTimeout(timeout)

    new RestTemplate(clientHttpRequestFactory)
  }

}

