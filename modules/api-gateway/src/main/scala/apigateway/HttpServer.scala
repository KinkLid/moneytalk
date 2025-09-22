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
import org.http4s.client.* // HTTP клиент
import org.http4s.ember.client.* // Клиент Ember
import scala.concurrent.duration.* // Длительности
import java.net.URI // Класс URI

// Простой HTTP-сервер с эндпоинтом здоровья
object HttpServer: // Объявляем объект сервера
  // Счётчик обращений к эндпоинтам
  private var metricHits: Long = 0L // Счётчик метрик
  // Базовые URL целевых сервисов из ENV
  private val usersApiBase: String = sys.env.getOrElse("USERS_API_URL", "http://localhost:8082") // URL users-api
  private val adminApiBase: String = sys.env.getOrElse("ADMIN_API_URL", "http://localhost:8083") // URL admin-api

  // Вспомогательная функция проксирования запроса
  private def proxyRequest(req: Request[cats.effect.IO], base: String, stripPrefix: String): cats.effect.IO[Response[cats.effect.IO]] = // Сигнатура
    EmberClientBuilder.default[cats.effect.IO].withTimeout(30.seconds).build.use { client => // Создаём клиент на время запроса
      val incomingPath = req.uri.path.renderString // Исходный путь
      val outPath = incomingPath.stripPrefix(stripPrefix) // Обрезаем префикс
      val target = uri"" // Пустой URI для сборки
      val newUri = Uri.unsafeFromString(base + outPath).withQueryParams(req.uri.query.multiParams) // Собираем конечный URI
      val outReq = req.withUri(newUri) // Заменяем URI в запросе
      client.run(outReq).use(r => cats.effect.IO.pure(r)) // Выполняем и возвращаем ответ
    } // Конец use

  // Роуты приложения
  private val routes: HttpRoutes[cats.effect.IO] = HttpRoutes.of[cats.effect.IO] { // Определяем маршруты
    case GET -> Root / "health" => Ok("OK") // Возвращаем 200 OK на /health
    case GET -> Root / "metrics" => // Эндпоинт метрик
      metricHits = metricHits + 1 // Увеличиваем счётчик
      Ok(s"api_gateway_metric_hits ${metricHits}\n").map(_.withContentType(org.http4s.headers.`Content-Type`(org.http4s.MediaType.text.plain))) // Отдаём text/plain
    case req if req.uri.path.renderString.startsWith("/users/") => // Проксируем на users-api
      JwtAuth.extractRole(req) match // Проверяем роль
        case Some(Role.User | Role.Admin) => proxyRequest(req, usersApiBase, "/users") // Разрешаем user/admin
        case _ => Forbidden("forbidden") // Нет доступа
    case req if req.uri.path.renderString.startsWith("/admin/") => // Проксируем на admin-api
      JwtAuth.extractRole(req) match // Проверяем роль
        case Some(Role.Admin) => proxyRequest(req, adminApiBase, "/admin") // Только admin
        case _ => Forbidden("forbidden") // Нет доступа
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

