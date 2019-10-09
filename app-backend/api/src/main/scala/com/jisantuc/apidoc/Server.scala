package com.jisantuc.apidoc.api

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.implicits._
import org.http4s.rho.RhoMiddleware
import org.http4s.rho.swagger.models._
import org.http4s.rho.swagger.syntax.{io => ioSwagger}
import org.http4s.server.blaze._
import org.http4s.server.middleware._
import org.http4s.server.Router

object ApiServer extends IOApp {

  val swaggerMiddleware: RhoMiddleware[IO] = ioSwagger.createRhoMiddleware(
    apiInfo = Info(
      title = "My API",
      version = "1.0.0",
      description = Some("functional because who hates sleep?")
    ),
    basePath = "/v1".some,
    schemes = List(Scheme.HTTPS)
  )

  val httpApp: HttpApp[IO] = CORS(
    Router(
      "/api/hello" -> HelloService.documented.toRoutes(swaggerMiddleware)
    )).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
