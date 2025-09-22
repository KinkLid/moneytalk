// Пакет тестов платежного сервиса
package paymentssvc // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: определена логика переходов статусов платежей
object StatusLogicSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем переходы для каждого события
  def spec = suite("Payment status transitions") ( // Название сьюта
    test("Given NEW When Init Then PENDING") { // Тест перехода NEW->PENDING
      val next = StatusLogic.next(PaymentStatus.NEW, PaymentEvent.Init) // Вычисляем следующий статус
      assertTrue(next == PaymentStatus.PENDING) // Проверяем результат
    }, // Конец теста
    test("Given PENDING When ProviderPaid Then PAID") { // Тест перехода в PAID
      val next = StatusLogic.next(PaymentStatus.PENDING, PaymentEvent.ProviderPaid) // Вычисляем статус
      assertTrue(next == PaymentStatus.PAID) // Проверяем результат
    }, // Конец теста
    test("Given PENDING When ProviderFailed Then FAILED") { // Тест перехода в FAILED
      val next = StatusLogic.next(PaymentStatus.PENDING, PaymentEvent.ProviderFailed) // Вычисляем статус
      assertTrue(next == PaymentStatus.FAILED) // Проверяем результат
    }, // Конец теста
    test("Given PENDING When ProviderExpired Then EXPIRED") { // Тест перехода в EXPIRED
      val next = StatusLogic.next(PaymentStatus.PENDING, PaymentEvent.ProviderExpired) // Вычисляем статус
      assertTrue(next == PaymentStatus.EXPIRED) // Проверяем результат
    }, // Конец теста
    test("Given NEW When ProviderPaid Then NEW") { // Тест устойчивости к невалидному событию
      val next = StatusLogic.next(PaymentStatus.NEW, PaymentEvent.ProviderPaid) // Вычисляем статус
      assertTrue(next == PaymentStatus.NEW) // Проверяем результат
    } // Конец теста
  ) // Конец сьюта

