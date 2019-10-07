package com.jisantuc.apidoc.datamodel

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

case class Foo(
    x: Int,
    y: String,
    z: Bar
)

object Foo {
  implicit val encFoo: Encoder[Foo] = deriveEncoder
  implicit val decFoo: Decoder[Foo] = deriveDecoder
}
