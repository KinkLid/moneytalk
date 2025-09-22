// Определяем пакет common для общих сущностей
package common // Указываем пакет для файла

// Импортируем ZIO для работы с эффектами
import zio.* // Подключаем ядро ZIO
import zio.config.* // Подключаем zio-config для конфигурации
import zio.config.magnolia.* // Импортируем автоматическое выведение конфигов
import zio.config.typesafe.* // Импортируем поддержку HOCON/ENV

// Определяем корневой конфиг приложения
final case class AppConfig(db: DbConfig, http: HttpConfig, mocks: MocksConfig) // Структура корневого конфига

// Конфиг БД
final case class DbConfig(url: String, user: String, password: String) // Параметры подключения к БД

// Конфиг HTTP
final case class HttpConfig(host: String, port: Int) // Параметры HTTP сервера

// Конфиг моков интеграций
final case class MocksConfig(enabled: Boolean) // Флаг включения моков

// Объект с ZLayer для загрузки конфига
object AppConfig: // Объявляем компаньон-объект
  // Упрощённый слой: читаем ENV с дефолтами, пока zio-config v4 не настроен
  val layer: ZLayer[Any, Nothing, AppConfig] = // Слой без ошибок
    ZLayer.succeed { // Возвращаем значение конфига
      val env = sys.env // Получаем карту переменных окружения
      val dbUrl = env.getOrElse("APP_DB_URL", "jdbc:postgresql://localhost:5432/laundry") // URL БД
      val dbUser = env.getOrElse("APP_DB_USER", "laundry") // Пользователь БД
      val dbPass = env.getOrElse("APP_DB_PASSWORD", "laundry") // Пароль БД
      val httpHost = env.getOrElse("APP_HTTP_HOST", "0.0.0.0") // Хост HTTP
      val httpPort = env.get("APP_HTTP_PORT").flatMap(_.toIntOption).getOrElse(8080) // Порт HTTP
      val mocksEnabled = env.get("MOCKS_ENABLED").exists(_.toLowerCase == "true") // Флаг мок-режима
      AppConfig(DbConfig(dbUrl, dbUser, dbPass), HttpConfig(httpHost, httpPort), MocksConfig(mocksEnabled)) // Собираем конфиг
    } // Конец ZLayer.succeed

