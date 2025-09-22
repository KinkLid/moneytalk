// Пакет для api-gateway
package apigateway // Объявляем пакет

// Импорты http4s и ZIO
import cats.effect.* // Импорт для интеграции с http4s (Cats Effect)
import org.http4s.* // Базовые типы http4s
import org.http4s.dsl.io.* // DSL для роутинга на IO
import org.http4s.ember.server.* // Лёгкий сервер http4s
import com.comcast.ip4s.* // Типы адресов/портов
import zio.ZIO // Импортируем только тип ZIO
import zio.interop.catz.* // Интероп ZIO <-> Cats Effect
import cats.syntax.semigroupk.* // Для комбинирования роутов оператором <+>
import org.http4s.headers.`WWW-Authenticate` // Заголовок WWW-Authenticate
import org.http4s.Challenge // Тип challenge

// Простой HTTP-сервер с эндпоинтом здоровья
object HttpServer: // Объявляем объект сервера
  // Роуты приложения
  private val routes: HttpRoutes[cats.effect.IO] = HttpRoutes.of[cats.effect.IO] { // Определяем маршруты
    case GET -> Root / "health" => Ok("OK") // Возвращаем 200 OK на /health
  } // Завершаем определение роутов

  // Защищённый маршрут whoami (только с ролью user или admin)
  private val whoamiRoutes: HttpRoutes[cats.effect.IO] = HttpRoutes.of[cats.effect.IO] { // Доп. маршруты
    case req @ GET -> Root / "whoami" => // Обрабатываем запрос
      JwtAuth.extractRole(req) match // Извлекаем роль
        case Some(r) => Ok(s"role=${r.toString.toLowerCase}") // Возвращаем строку роли
        case None    => Unauthorized(`WWW-Authenticate`(Challenge("Bearer", "api-gateway"))) // Нет роли
  } // Конец маршрутов whoami

  // Комбинируем публичные и защищённые маршруты
  private val allRoutes: HttpRoutes[cats.effect.IO] = // Объединяем
    routes <+> JwtAuth.requireRole(Role.User)(whoamiRoutes) // Применяем миддлварь роли user

  // Запуск сервера как ZIO-эффект
  def serve(port: Int): ZIO[Any, Throwable, Unit] = // Определяем метод запуска с портом
    ZIO.runtime[Any].flatMap { _ => // Получаем рантайм ZIO (не используем явно)
      val httpApp = allRoutes.orNotFound // Превращаем маршруты в приложение
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

  // Публичный httpApp для тестов
  def httpApp: org.http4s.HttpApp[cats.effect.IO] = allRoutes.orNotFound // Экспортируем HttpApp

