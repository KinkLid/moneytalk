// Определяем пакет common для общих утилит
package common // Задаём пакет

// Импортируем ZIO и логирование
import zio.* // Импорт ядра ZIO
import zio.logging.* // Импортируем zio-logging
import zio.logging.backend.SLF4J // Импортируем бэкенд SLF4J

// Объект для инициализации логирования
object Logging: // Объявляем объект Logging
  // Слой логирования через SLF4J
  val layer: ZLayer[Any, Nothing, Unit] = // Определяем слой без ошибок
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j // Убираем дефолтные логгеры и подключаем SLF4J

