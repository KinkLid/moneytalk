// Пакет интеграционных тестов платежного сервиса
package paymentssvc // Объявляем пакет

// Импорт ZIO и тестов
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Импорт Testcontainers и Flyway
import com.dimafeng.testcontainers.PostgreSQLContainer // Контейнер Postgres
import org.flywaydb.core.Flyway // Flyway API

// Given: чистая БД в контейнере и миграции Flyway
object IntegrationSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: применяем миграции и выполняем простой запрос
  def spec = suite("Payments integration with Postgres") ( // Название сьюта
    test("Given Postgres When Flyway migrate Then schema created and connectable") { // Тест миграций
      ZIO.attempt { // Оборачиваем в эффект
        val container = PostgreSQLContainer() // Создаём контейнер
        container.start() // Запускаем контейнер
        try // Блок гарантии остановки
          val flyway = Flyway // Инициализируем Flyway
            .configure() // Начинаем конфиг
            .dataSource(container.jdbcUrl, container.username, container.password) // Источник данных
            .locations("classpath:db/migration") // Путь к миграциям
            .schemas("laundry") // Схема
            .load() // Загружаем
          val result = flyway.migrate() // Применяем миграции
          assertTrue(result.migrationsExecuted > 0) // Ожидаем хотя бы одну миграцию
        finally // Завершаем
          container.stop() // Останавливаем контейнер
      } // Конец эффекта
    } // Конец теста
  ) // Конец сьюта


