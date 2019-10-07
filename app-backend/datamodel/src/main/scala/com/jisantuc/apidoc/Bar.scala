package com.jisantuc.apidoc.datamodel

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.deriveDecoder

case class Bar(
    x: Int,
    y: String
)

object Bar {
  implicit val encBar: Decoder[Bar] = deriveDecoder
  implicit val decBar: Encoder[Bar] = Encoder.forProduct3(
    "x",
    "y",
    "deprecated"
  )((b: Bar) => (b.x, b.y, false))
}
