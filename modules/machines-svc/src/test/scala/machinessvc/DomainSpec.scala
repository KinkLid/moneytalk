// Пакет тестов machines-svc
package machinessvc // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: доменная логика доступности машины и поиска программ
object DomainSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем обе функции на позитивных и негативных кейсах
  def spec = suite("Machine domain logic") ( // Название сьюта
    test("Given active machine When ensureAvailable Then Right(())") { // Проверка позитивного кейса доступности
      val m = Machine("m1", isActive = true) // Создаём активную машину
      val res = MachineService.ensureAvailable(m) // Вызываем проверку
      assertTrue(res.isRight) // Должно быть Right
    }, // Конец теста
    test("Given inactive machine When ensureAvailable Then Left(MachineInactive)") { // Проверка негативного кейса доступности
      val m = Machine("m2", isActive = false) // Создаём неактивную машину
      val res = MachineService.ensureAvailable(m) // Вызываем проверку
      assertTrue(res == Left(MachineError.MachineInactive)) // Должен быть код ошибки
    }, // Конец теста
    test("Given programs When findProgram exists Then returns program") { // Проверка успешного поиска программы
      val ps = List(Program("Q30", "Quick 30", 30, 1000)) // Список программ
      val res = MachineService.findProgram(ps, "Q30") // Ищем программу
      assertTrue(res.exists(_.code == "Q30")) // Находим нужную
    }, // Конец теста
    test("Given programs When findProgram missing Then ProgramNotFound") { // Проверка ошибки поиска
      val ps = List(Program("Q30", "Quick 30", 30, 1000)) // Список программ
      val res = MachineService.findProgram(ps, "C60") // Ищем отсутствующую
      assertTrue(res == Left(MachineError.ProgramNotFound)) // Должна быть ошибка
    } // Конец теста
  ) // Конец сьюта

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

