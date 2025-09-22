// Пакет тестов email-svc
package emailsvc // Объявляем пакет

// Импорт ZIO Test и http4s клиента
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений
import cats.effect.IO // Cats IO
import cats.effect.unsafe.implicits.global // Рантайм IO
import org.http4s.client.Client // Клиент http4s
import org.http4s.* // Типы http4s
import org.http4s.dsl.io.* // DSL
import org.http4s.implicits.* // uri литералы

// Given: httpApp сервиса
object HealthSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем /health
  def spec = suite("Email health") ( // Название сьюта
    test("Given httpApp When GET /health Then 200 OK") { // Проверка здоровья
      ZIO.attempt { // Оборачиваем в эффект
        val app = HttpServer.httpApp // Получаем httpApp
        val client = Client.fromHttpApp(app) // Создаём клиент
        val status = client.run(Request[IO](Method.GET, uri"/health")).use(r => IO.pure(r.status)).unsafeRunSync() // Делаем запрос
        assertTrue(status == Status.Ok) // Ожидаем 200
      } // Завершение эффекта
    } // Конец теста
  ) // Конец сьюта


