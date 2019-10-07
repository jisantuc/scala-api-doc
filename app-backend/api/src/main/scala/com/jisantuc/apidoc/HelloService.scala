package com.jisantuc.apidoc.api

import com.jisantuc.apidoc.datamodel._

import cats.effect._
import io.circe._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.dsl.Http4sDsl

object HelloService extends Http4sDsl[IO] {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / name =>
      Ok(Json.obj("message" -> Json.fromString(s"Hello, ${name}")))

    case req @ POST -> Root / "foo" =>
      for {
        foo <- req.as[Foo].attempt
        resp <- foo match {
          case Right(f) => Ok(f.asJson)
          case Left(e)  => BadRequest(e.getMessage)
        }
      } yield resp
  }
}
