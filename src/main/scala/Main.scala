import zio.*
import zio.http.*

object Main extends ZIOAppDefault:

  private val routes: Routes[Any, Nothing] = Routes(
    Method.GET / "health" -> handler(Response.text("OK")),
    Method.GET / "hello" -> handler(Response.text("Hello, ZIO!"))
  )

  private val app: HttpApp[Any] = routes.toHttpApp

  override def run: ZIO[Any, Any, Any] =
    Server.serve(app)
      .provide(
        Server.defaultWithPort(8080)
      )


