package com.jisantuc.apidoc.datamodel

import cats.implicits._
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import io.circe.syntax._

sealed trait Foo

object Foo {

  import SmallFoo._
  import BigFoo._

  implicit val encFoo: Encoder[Foo] = new Encoder[Foo] {

    def apply(f: Foo) = f match {
      case big: BigFoo     => big.asJson
      case small: SmallFoo => small.asJson
    }
  }

  implicit val decFoo: Decoder[Foo] =
    Decoder[SmallFoo].widen or Decoder[BigFoo].widen
}

case class SmallFoo(x: Int) extends Foo

object SmallFoo {
  implicit val encSmall: Encoder[SmallFoo] = deriveEncoder
  implicit val decSmall: Decoder[SmallFoo] = deriveDecoder
}

case class BigFoo(
    x: Int,
    y: String,
    z: Bar
) extends Foo

object BigFoo {
  implicit val encBig: Encoder[BigFoo] = deriveEncoder
  implicit val decBig: Decoder[BigFoo] = deriveDecoder
}
