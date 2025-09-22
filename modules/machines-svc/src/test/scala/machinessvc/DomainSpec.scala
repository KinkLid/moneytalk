// Пакет тестов machines-svc
package machinessvc // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: определены модели Machine/Program и сервис MachineService
object DomainSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем доступность и поиск программ
  def spec = suite("Machines domain") ( // Название набора
    test("Given active machine When ensureAvailable Then Right(())") { // Проверка активной машины
      val m = Machine("m1", isActive = true) // Создаём машину
      val res = MachineService.ensureAvailable(m) // Проверяем доступность
      assertTrue(res.isRight) // Должно быть Right
    }, // Конец теста
    test("Given inactive machine When ensureAvailable Then Left(MachineInactive)") { // Проверка неактивной машины
      val m = Machine("m1", isActive = false) // Создаём машину
      val res = MachineService.ensureAvailable(m) // Проверяем доступность
      assertTrue(res == Left(MachineError.MachineInactive)) // Ожидаем ошибку
    }, // Конец теста
    test("Given programs When findProgram with existing code Then Program") { // Поиск существующей программы
      val ps = List(Program("Q30", "Quick 30", 30, 20000)) // Список программ
      val res = MachineService.findProgram(ps, "Q30") // Ищем по коду
      assertTrue(res == Right(ps.head)) // Ожидаем найденную программу
    }, // Конец теста
    test("Given programs When findProgram with missing code Then ProgramNotFound") { // Поиск отсутствующей программы
      val ps = List(Program("Q30", "Quick 30", 30, 20000)) // Список программ
      val res = MachineService.findProgram(ps, "DELI") // Ищем несуществующий код
      assertTrue(res == Left(MachineError.ProgramNotFound)) // Ожидаем ошибку
    } // Конец теста
  ) // Конец набора

