// Пакет тестов api-gateway
package apigateway // Объявляем пакет

// Импорт ZIO и тестов
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Импорт http4s клиента
import cats.effect.IO // Cats IO
import cats.effect.unsafe.implicits.global // Рантайм IO
import org.http4s.client.Client // Клиент
import org.http4s.* // Типы
import org.http4s.dsl.io.* // DSL
import org.http4s.implicits.* // uri
import org.http4s.headers.Authorization // Заголовок Authorization

// Given: httpApp api-gateway
object AuthSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем доступ к /whoami с разными токенами
  def spec = suite("API Gateway auth") ( // Название набора
    test("Given Bearer user When GET /whoami Then 200 and role=user") { // Проверка роли user
      ZIO.attempt {
        val app = HttpServer.httpApp // Получаем приложение
        val client = Client.fromHttpApp(app) // Клиент
        val req = Request[IO](Method.GET, uri"/whoami").putHeaders(Authorization(Credentials.Token(AuthScheme.Bearer, "user"))) // Запрос с токеном
        val body = client.run(req).use(_.as[String]).unsafeRunSync() // Выполняем запрос
        assertTrue(body.contains("role=user")) // Проверяем роль
      }
    },
    test("Given no token When GET /whoami Then 403") { // Проверка без токена
      ZIO.attempt {
        val app = HttpServer.httpApp // Приложение
        val client = Client.fromHttpApp(app) // Клиент
        val req = Request[IO](Method.GET, uri"/whoami") // Без токена
        val status = client.run(req).use(r => IO.pure(r.status)).unsafeRunSync() // Выполняем
        assertTrue(status == Status.Forbidden) // Должен быть 403
      }
    }
  )

