// Пакет тестов платежного сервиса
package paymentssvc // Объявляем пакет

// Импорт ZIO и тестов
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: мок-клиент Сбера
object SberClientSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем методы мока
  def spec = suite("Sber client mock") ( // Название сьюта
    test("Given order When initPayment Then returns pm_<order>") { // Проверка initPayment
      (for // Начинаем ZIO for-comprehension
        client <- ZIO.service[SberClient] // Получаем клиента из слоя
        pid <- client.initPayment("ord1", 123) // Вызываем инициацию
      yield assertTrue(pid == "pm_ord1")).provide(MockSberClient.layer) // Проверяем значение
    }, // Конец теста
    test("Given paymentId When getStatus Then PAID") { // Проверка статуса
      (for // Начинаем цепочку
        client <- ZIO.service[SberClient] // Получаем клиента
        st <- client.getStatus("pm_1") // Вызываем статус
      yield assertTrue(st == PaymentStatus.PAID)).provide(MockSberClient.layer) // Ожидаем PAID
    }, // Конец теста
    test("Given payload When verifyCallbackSignature Then true") { // Проверка подписи
      (for // Начинаем цепочку
        client <- ZIO.service[SberClient] // Получаем клиента
        ok <- client.verifyCallbackSignature("{}", "sig") // Вызываем проверку
      yield assertTrue(ok)).provide(MockSberClient.layer) // Должно быть true
    } // Конец теста
  ) // Конец набора

