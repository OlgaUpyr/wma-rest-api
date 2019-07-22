package com.wma

import java.io.IOException

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import play.api.libs.json.JsValue

class WMAJsonObjectScala extends StdSerializer[JsValue](classOf[JsValue]) {

  @throws[IOException]
  override def serialize(value: JsValue, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
    jgen.writeRawValue(value.toString)
  }
}

