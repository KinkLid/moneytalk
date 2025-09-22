// Пакет для lg-adapter
package lgadapter // Объявляем пакет

// Импорты http4s и ZIO
import cats.effect.* // Импорт для интеграции с http4s (Cats Effect)
import org.http4s.* // Базовые типы http4s
import org.http4s.dsl.io.* // DSL для роутинга на IO
import org.http4s.ember.server.* // Лёгкий сервер http4s
import com.comcast.ip4s.* // Типы адресов/портов
import zio.ZIO // Импортируем только тип ZIO
import zio.interop.catz.* // Интероп ZIO <-> Cats Effect
import org.http4s.circe.* // Поддержка Circe в http4s
import io.circe.syntax.* // Синтаксис .asJson
import lgadapter.JsonCodecs.given // Имплиситы кодеков JSON

// Простой HTTP-сервер с эндпоинтом здоровья
object HttpServer: // Объявляем объект сервера
  // Счётчик обращений
  private var metricHits: Long = 0L // Счётчик
  // Роуты приложения
  private val routes: HttpRoutes[cats.effect.IO] = HttpRoutes.of[cats.effect.IO] { // Определяем маршруты
    case GET -> Root / "health" => Ok("OK") // Возвращаем 200 OK на /health
    case GET -> Root / "metrics" => // Эндпоинт метрик
      metricHits = metricHits + 1 // Инкремент
      Ok(s"lg_adapter_metric_hits ${metricHits}\n").map(_.withContentType(org.http4s.headers.`Content-Type`(org.http4s.MediaType.text.plain))) // Текстовая метрика
    case req @ POST -> Root / "startCycle" => // Эндпоинт запуска цикла
      req.asJsonDecode[StartCycleRequest].flatMap { body => // Декодируем JSON тела
        // В мок-режиме просто возвращаем 200
        Ok(()) // Успех без содержимого
      }
    case GET -> Root / "status" / machineId => // Эндпоинт статуса машины
      Ok(StatusResponse(machineId, "IDLE").asJson) // Возвращаем мок-статус
  } // Завершаем определение роутов

  // Публичный httpApp для модульных тестов
  def httpApp: org.http4s.HttpApp[cats.effect.IO] = routes.orNotFound // Экспортируем HttpApp

  // Запуск сервера как ZIO-эффект
  def serve(port: Int): ZIO[Any, Throwable, Unit] = // Определяем метод запуска с портом
    ZIO.runtime[Any].flatMap { _ => // Получаем рантайм ZIO (не используем явно)
      val httpApp = routes.orNotFound // Превращаем маршруты в приложение
      val io = EmberServerBuilder.default[cats.effect.IO] // Создаём конфигурацию сервера
        .withHost(ipv4"0.0.0.0") // Слушаем на всех интерфейсах
        .withPort(Port.fromInt(port).get) // Указываем порт
        .withHttpApp(httpApp) // Устанавливаем приложение
        .build // Строим ресурс сервера
        .useForever // Держим сервер бесконечно
      ZIO.attempt { // Оборачиваем выполнение в ZIO
        import cats.effect.unsafe.implicits.global // Импортируем глобальный рантайм CE
        io.unsafeRunAndForget() // Запускаем неблокирующе
      } *> ZIO.never // Никогда не завершаем эффект
    } // Завершаем создание эффекта

