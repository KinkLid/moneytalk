// Пакет тестов users-api
package usersapi // Объявляем пакет

// Импорт ZIO и тестов
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Импорт http4s клиента
import cats.effect.IO // Cats IO
import cats.effect.unsafe.implicits.global // Рантайм IO
import org.http4s.client.Client // Клиент http4s
import org.http4s.* // Типы
import org.http4s.dsl.io.* // DSL
import org.http4s.implicits.* // uri

// Given: httpApp users-api
object MetricsSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: вызываем /health, затем /metrics и проверяем метрику
  def spec = suite("Users API metrics") ( // Название набора
    test("Given health hits When GET /metrics Then contains counter") { // Описание теста
      ZIO.attempt { // Выполняем в эффекте
        val app = HttpServer.httpApp // Получаем приложение
        val client = Client.fromHttpApp(app) // Создаём клиент
        val reqHealth = Request[IO](Method.GET, uri"/health") // Запрос к /health
        client.run(reqHealth).use(_ => IO.unit).unsafeRunSync() // Делаем вызов
        val reqMetrics = Request[IO](Method.GET, uri"/metrics") // Запрос к /metrics
        val body = client.run(reqMetrics).use(_.as[String]).unsafeRunSync() // Читаем тело
        assertTrue(body.contains("users_api_health_hits")) // Проверяем наличие метрики
      } // Завершаем эффект
    } // Конец теста
  ) // Конец набора

