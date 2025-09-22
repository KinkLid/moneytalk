// Пакет тестов users-api
package usersapi // Объявляем пакет

// Импорт ZIO и тестов
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Импорт http4s клиента и утилит
import cats.effect.IO // Cats IO
import cats.effect.unsafe.implicits.global // Рантайм IO
import org.http4s.client.Client // Клиент http4s
import org.http4s.* // Базовые типы
import org.http4s.dsl.io.* // DSL
import org.http4s.implicits.* // Имплиситы uri

// Given: httpApp users-api
object MachinesSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: вызываем /machines и проверяем 200
  def spec = suite("Users API machines") ( // Название набора
    test("Given httpApp When GET /machines Then 200 OK") { // Описание теста
      ZIO.attempt { // Выполняем в эффекте
        val app = HttpServer.httpApp // Получаем приложение
        val client = Client.fromHttpApp(app) // Создаём клиент
        val req = Request[IO](Method.GET, uri"/machines") // Запрос
        val status = client.run(req).use(r => IO.pure(r.status)).unsafeRunSync() // Выполняем запрос
        assertTrue(status == Status.Ok) // Проверяем статус
      } // Завершаем эффект
    } // Конец теста
  ) // Конец набора

