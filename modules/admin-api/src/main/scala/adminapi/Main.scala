// Пакет для admin-api
package adminapi // Объявляем пакет

// Импорт ZIO
import zio.* // Импорт ядра ZIO

// Точка входа admin-api с запуском HTTP сервера
object Main extends ZIOAppDefault: // Объявляем main-объект
  // Метод run
  def run: ZIO[Any, Any, Any] = // Определяем run
    HttpServer.serve(8083) // Запуск сервера на 8083

