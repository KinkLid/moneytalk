// Указываем пакет для api-gateway
package apigateway // Объявляем пакет

// Импортируем ZIO для точки входа
import zio.* // Импорт ядра ZIO

// Приложение api-gateway, поднимает http-сервер с /health
object Main extends ZIOAppDefault: // Объявляем объект входа приложения
  // Реализация run-запуска
  def run: ZIO[Any, Any, Any] = // Определяем метод run
    HttpServer.serve(8081) // Запускаем сервер на 8081

