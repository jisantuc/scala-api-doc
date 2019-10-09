package com.jisantuc.apidoc.api

import com.jisantuc.apidoc.datamodel._

import cats.effect._
import io.circe._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.rho._
import org.http4s.rho.swagger.syntax.io._

object HelloService extends Http4sDsl[IO] {

  val documented = new RhoRoutes[IO] {
    List(
      "greet someone" ** GET / pathVar[String]("name", "Who to greet") |>> { (name: String) =>
        Ok(Json.obj("message" -> Json.fromString(s"Hello, ${name}")))
      },
      "echo a foo" ** POST / "foo" ^ jsonOf[IO, Foo] |>> { (foo: Foo) =>
        Ok(foo.asJson)
      }
    )
  }
}
