// Пакет тестов users-api
package usersapi // Объявляем пакет

// Импорты для тестов ZIO
import zio.ZIO // Импортируем только ZIO, чтобы не конфликтовать с Cats IO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Импорты http4s-клиента для локального тестирования
import cats.effect.IO // Импортируем Cats Effect IO
import org.http4s.client.dsl.io.* // DSL клиента
import org.http4s.* // Базовые типы http4s
import org.http4s.dsl.io.* // DSL http4s
import cats.syntax.all.* // Утилиты Cats для теста
import org.http4s.implicits.* // Имплиситы для литералов uri
import org.http4s.client.Client // Клиент http4s
import cats.effect.unsafe.implicits.global // Имплиситный рантайм IO для unsafeRunSync

// Given: поднятый http-приложение с роутом /health
object HealthSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When: обращаемся к /health
  def spec = suite("Health endpoint") ( // Группа тестов для /health
    test("Given httpApp When GET /health Then 200 OK") { // Then: получаем 200 OK
      ZIO.attempt { // Given: in-memory клиент
        val app = HttpServer.httpApp // Берём httpApp
        val client = Client.fromHttpApp(app) // Создаём клиент поверх httpApp
        val req = Request[IO](Method.GET, uri"/health") // When: запрос GET /health
        val status = client.run(req).use(r => IO.pure(r.status)).unsafeRunSync() // Then: получаем статус
        assertTrue(status == Status.Ok) // Проверяем 200 OK
      } // Завершение эффекта теста
    } // Конец теста
  ) // Конец набора

