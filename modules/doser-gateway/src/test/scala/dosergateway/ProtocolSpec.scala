// Пакет тестов doser-gateway
package dosergateway // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: протокол дозатора с построением команд и парсингом ответов
object ProtocolSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем корректные строки и ошибки
  def spec = suite("Doser protocol") ( // Название сьюта
    test("Given channel D and ml 50 When buildDoseCommand Then DOSE:D:50\\n") { // Проверка построения команды
      val cmd = Protocol.buildDoseCommand("D", 50) // Строим команду
      assertTrue(cmd == "DOSE:D:50\n") // Ожидаем точное совпадение
    }, // Конец теста
    test("Given 'OK' When parseResponse Then Ok") { // Парсинг успешного ответа
      val r = Protocol.parseResponse("OK\n") // Парсим строку
      assertTrue(r == DoseResult.Ok) // Должен быть Ok
    }, // Конец теста
    test("Given 'ERR:TIMEOUT' When parseResponse Then Err('TIMEOUT')") { // Парсинг ошибки
      val r = Protocol.parseResponse("ERR:TIMEOUT\n") // Парсим строку
      assertTrue(r == DoseResult.Err("TIMEOUT")) // Должен быть соответствующий код
    } // Конец теста
  ) // Конец сьюта

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

