// Пакет тестов doser-gateway
package dosergateway // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: определён протокол дозатора
object ProtocolSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем построение команд и разбор ответов
  def spec = suite("Doser protocol") ( // Название набора
    test("Given D and 50 ml When buildDoseCommand Then DOSE:D:50\\n") { // Проверяем построение команды
      val cmd = Protocol.buildDoseCommand("D", 50) // Строим команду
      assertTrue(cmd == "DOSE:D:50\n") // Ожидаемая строка
    }, // Конец теста
    test("Given OK\\n When parseResponse Then Ok") { // Проверяем разбор OK
      val res = Protocol.parseResponse("OK\n") // Парсим строку
      assertTrue(res == DoseResult.Ok) // Ожидаем успех
    }, // Конец теста
    test("Given ERR:42\\n When parseResponse Then Err(42)") { // Проверяем разбор ошибки
      val res = Protocol.parseResponse("ERR:42\n") // Парсим ошибку
      assertTrue(res == DoseResult.Err("42")) // Ожидаем код 42
    } // Конец теста
  ) // Конец набора

