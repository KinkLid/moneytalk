import zio.*
import zio.http.*

object Main extends ZIOAppDefault:
  private val app =
    Routes(
      Method.GET / ""      -> handler(Response.text("OK")),
      Method.GET / "hello"  -> handler(Response.text("Hello, ZIO!"))
    )

  override def run: ZIO[Any, Throwable, Nothing] =
    Server.serve(app).provide(Server.default)

