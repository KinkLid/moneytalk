// Пакет тестов lg-adapter
package lgadapter // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: мок-клиент LG облака
object ClientSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем методы мока
  def spec = suite("LG client mock") ( // Название сьюта
    test("Given machine and program When startCycle Then success") { // Проверяем запуск цикла
      (for // Начинаем ZIO for
        c <- ZIO.service[LGCloudClient] // Получаем клиента
        _ <- c.startCycle("m1", "Q30") // Вызываем запуск
      yield assertTrue(true)).provide(MockLGCloudClient.layer) // Успех означает true
    }, // Конец теста
    test("Given machine When status Then IDLE") { // Проверяем статус
      (for // Начинаем ZIO for
        c <- ZIO.service[LGCloudClient] // Получаем клиента
        s <- c.status("m1") // Получаем статус
      yield assertTrue(s == "IDLE")).provide(MockLGCloudClient.layer) // Ожидаем IDLE
    } // Конец теста
  ) // Конец набора


