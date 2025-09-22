// Пакет для users-api HTTP сервера
package usersapi // Объявляем пакет

// Импорты http4s и ZIO
import cats.effect.* // Импорт для интеграции с http4s (Cats Effect)
import org.http4s.* // Базовые типы http4s
import org.http4s.dsl.io.* // DSL для роутинга на IO
import org.http4s.ember.server.* // Лёгкий сервер http4s
import com.comcast.ip4s.* // Импорт типов для адресов/портов
import org.http4s.circe.* // Поддержка Circe в http4s
import org.http4s.headers.`Content-Type` // Заголовок Content-Type
import org.http4s.MediaType // Типы медиа
import io.circe.syntax.* // Синтаксис .asJson
import usersapi.JsonCodecs.given // Имплиситы кодеков
import usersapi.* // Импортируем модели
import zio.ZIO // Импортируем только тип ZIO, чтобы избежать конфликта IO
import zio.interop.catz.* // Интероп ZIO <-> Cats Effect

// Простой HTTP-сервер с эндпоинтом здоровья
object HttpServer: // Объявляем объект сервера
  // Простой счётчик обращений к /health
  private var healthHits: Long = 0L // Счётчик обращений

  // Роуты приложения
  private val routes: HttpRoutes[cats.effect.IO] = HttpRoutes.of[cats.effect.IO] { // Определяем маршруты на Cats IO
    case GET -> Root / "health" => // Возвращаем 200 OK на /health
      healthHits = healthHits + 1 // Увеличиваем счётчик
      Ok("OK") // Отдаём ответ
    case GET -> Root / "machines" => // Эндпоинт списка машин
      val data = MachinesResponse( // Формируем ответ
        List(MachineDto("m1", "LG WD-M0C7FD3S", active = true)) // Пример одной машины
      ) // Конец построения
      Ok(data.asJson) // Отдаём JSON
    case GET -> Root / "metrics" => // Эндпоинт метрик Prometheus-подобный
      val body = s"users_api_health_hits ${healthHits}\n" // Формируем метрику
      Ok(body).map(_.withContentType(`Content-Type`(MediaType.text.plain))) // Отдаём text/plain
  } // Завершаем определение роутов

  // Публичный httpApp для тестов
  def httpApp: org.http4s.HttpApp[cats.effect.IO] = routes.orNotFound // Экспортируем HttpApp для in-memory тестов

  // Запуск сервера как ZIO-эффект
  def serve(port: Int): ZIO[Any, Throwable, Unit] = // Определяем метод запуска с портом
    ZIO.runtime[Any].flatMap { implicit rt => // Получаем рантайм ZIO
      val httpApp = routes.orNotFound // Превращаем маршруты в приложение
      val io = EmberServerBuilder.default[cats.effect.IO] // Создаём конфигурацию сервера
        .withHost(ipv4"0.0.0.0") // Слушаем на всех интерфейсах
        .withPort(Port.fromInt(port).get) // Указываем порт
        .withHttpApp(httpApp) // Устанавливаем приложение
        .build // Строим ресурс сервера
        .useForever // Держим сервер в живом состоянии
      ZIO.attempt { // Оборачиваем запуск в ZIO эффект
        import cats.effect.unsafe.implicits.global // Импортируем глобальный рантайм Cats Effect
        io.unsafeRunAndForget() // Запускаем сервер неблокирующе
      } *> ZIO.never // Никогда не завершаем эффект, чтобы сервер жил
    } // Завершаем создание эффекта

