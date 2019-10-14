package com.jisantuc.apidoc.api

import com.jisantuc.apidoc.datamodel._

import cats.effect._
import cats.implicits._
import io.circe._
import org.http4s._
import tapir._
import tapir.json.circe._
import tapir.server.http4s._
import tapir.openapi.OpenAPI
import tapir.openapi.circe.yaml._
import tapir.docs.openapi._
import tapir.swagger.http4s.SwaggerHttp4s

import scala.language.higherKinds

object HelloRouter {

  val greetEndpoint: Endpoint[String, Unit, Json, Nothing] =
    endpoint.get
      .in("greet")
      .in(path[String])
      .out(jsonBody[Json])
      .description("Greet someone")
      .name("greet")

  val fooEndpoint: Endpoint[Foo, Unit, Foo, Nothing] =
    endpoint.post
      .in("foo")
      .in(jsonBody[Foo])
      .out(jsonBody[Foo])
      .description("Echo a Foo")
      .name("echo")

  val endpoints = List(greetEndpoint, fooEndpoint)

}

class HelloService(implicit val contextShift: ContextShift[IO]) {

  def greetRoute: HttpRoutes[IO] = HelloRouter.greetEndpoint.toRoutes(greet _)
  def fooRoute: HttpRoutes[IO]   = HelloRouter.fooEndpoint.toRoutes(foo _)
  val doc: OpenAPI               = HelloRouter.endpoints.toOpenAPI("Hello Service", "1.0")
  def docRoutes: HttpRoutes[IO]  = new SwaggerHttp4s(doc.toYaml).routes

  def greet(name: String): IO[Either[Unit, Json]] =
    IO.pure { Right(Json.obj("message" -> Json.fromString(s"Hello, ${name}"))) }

  def foo(foo: Foo): IO[Either[Unit, Foo]] =
    IO.pure { Right(foo) }

  val routes: HttpRoutes[IO] =
    greetRoute <+> fooRoute <+> docRoutes
}
