// Указываем пакет для users-api
package usersapi // Объявляем пакет

// Импортируем ZIO
import zio.* // Подключаем ядро ZIO

// Приложение users-api, поднимает http-сервер с /health
object Main extends ZIOAppDefault: // Объявляем входную точку
  // Тело исполнения
  def run: ZIO[Any, Any, Any] = // Определяем run
    HttpServer.serve(8082) // Запускаем сервер на 8082

