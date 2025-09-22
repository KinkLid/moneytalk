// Пакет для fiscal-svc
package fiscalsvc // Объявляем пакет

// Импорт ZIO
import zio.* // Импорт ядра ZIO

// Минимальная точка входа
object Main extends ZIOAppDefault: // Объявляем объект входа
  // Метод run
  def run: ZIO[Any, Any, Any] = // Определяем run
    HttpServer.serve(8088) // Запускаем сервер на 8088

