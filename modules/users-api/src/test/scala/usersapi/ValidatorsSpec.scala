// Пакет тестов users-api
package usersapi // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: валидаторы DTO
object ValidatorsSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем ошибки и успешные случаи
  def spec = suite("Validators") ( // Название набора
    test("Given empty id When validateMachine Then EmptyId") { // Проверка пустого id
      val res = Validators.validateMachine(MachineDto(" ", "m", active = true)) // Валидируем машину
      assertTrue(res == Left(ValidationError.EmptyId)) // Ожидаем ошибку
    }, // Конец теста
    test("Given negative price When validateProgram Then NonPositivePrice") { // Проверка цены
      val res = Validators.validateProgram(ProgramDto("Q", "Quick", -1, 30)) // Валидируем программу
      assertTrue(res == Left(ValidationError.NonPositivePrice)) // Ожидаем ошибку
    }, // Конец теста
    test("Given zero duration When validateProgram Then NonPositiveDuration") { // Проверка длительности
      val res = Validators.validateProgram(ProgramDto("Q", "Quick", 100, 0)) // Валидируем программу
      assertTrue(res == Left(ValidationError.NonPositiveDuration)) // Ожидаем ошибку
    }, // Конец теста
    test("Given valid program When validateProgram Then Right") { // Успешная валидация
      val res = Validators.validateProgram(ProgramDto("Q", "Quick", 100, 30)) // Валидируем программу
      assertTrue(res.isRight) // Ожидаем успех
    } // Конец теста
  ) // Конец набора

