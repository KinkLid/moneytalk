// Пакет для api-gateway
package apigateway // Объявляем пакет

// Импорт http4s и ZIO
import org.http4s.* // Базовые типы http4s
import org.http4s.dsl.io.* // DSL для http4s
import cats.data.{Kleisli, OptionT} // Функциональный конвейер и OptionT
import cats.effect.IO // Cats IO
import org.http4s.headers.Authorization // Заголовок авторизации
import org.http4s.Credentials // Типы креденшалов
import org.http4s.AuthScheme // Схема авторизации

// Роли пользователей
enum Role: // Перечисление ролей
  case User // Пользователь
  case Admin // Администратор

// Примитивная валидация JWT (мок): токен "user" => User, "admin" => Admin
object JwtAuth: // Объявляем объект аутентификации
  // Извлекаем роль из заголовка Authorization: Bearer <token>
  def extractRole(req: Request[IO]): Option[Role] = // Сигнатура функции
    req.headers.get[Authorization].collect { // Пытаемся получить заголовок
      case Authorization(Credentials.Token(AuthScheme.Bearer, "user"))  => Role.User // Токен user
      case Authorization(Credentials.Token(AuthScheme.Bearer, "admin")) => Role.Admin // Токен admin
    } // Конец извлечения

  // Миддлварь авторизации по требуемой роли
  def requireRole(required: Role)(routes: HttpRoutes[IO]): HttpRoutes[IO] = // Сигнатура миддлвари
    Kleisli { (req: Request[IO]) => // Описываем обработчик
      extractRole(req) match // Пытаемся извлечь роль
        case Some(role) if role == required || role == Role.Admin => // Разрешаем Admin на все
          routes.run(req) // Пропускаем к маршрутам
        case _ => // Иначе запрещаем
          OptionT.liftF(Forbidden("forbidden")) // Возвращаем 403
    } // Конец Kleisli

