// Пакет тестов fiscal-svc
package fiscalsvc // Объявляем пакет

// Импорт ZIO и тестов
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: мок-клиент OrangeData
object OrangeDataClientSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем создание чека и статус
  def spec = suite("OrangeData client mock") ( // Название сьюта
    test("Given payload When createReceipt Then returns id") { // Проверка createReceipt
      (for // Цепочка ZIO
        client <- ZIO.service[OrangeDataClient] // Получаем клиента
        id <- client.createReceipt(Map("a" -> 1)) // Создаём чек
      yield assertTrue(id.nonEmpty)).provide(MockOrangeDataClient.layer) // Проверяем непустоту id
    }, // Конец теста
    test("Given id When getStatus Then DONE") { // Проверка статуса
      (for // Цепочка ZIO
        client <- ZIO.service[OrangeDataClient] // Получаем клиента
        st <- client.getStatus("rcpt_1") // Получаем статус
      yield assertTrue(st == "DONE")).provide(MockOrangeDataClient.layer) // Ожидаем DONE
    } // Конец теста
  ) // Конец набора

