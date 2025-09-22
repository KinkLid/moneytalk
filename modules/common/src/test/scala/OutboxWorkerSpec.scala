// Пакет тестов для common
package common // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: InMemory репозиторий и мок-обработчик
object OutboxWorkerSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем успех, повтор и DLQ
  def spec = suite("Outbox worker") ( // Название сьюта
    test("Given success handler When processOnce Then marks DONE") { // Успешная обработка
      for // Композиция эффектов
        ref <- Ref.make(List(OutboxEvent("1", "EMAIL", "{}", 0, "NEW"))) // Инициализируем состояние
        repo = InMemoryOutboxRepository(ref) // Репозиторий
        handler = MockEventHandler(_ => true) // Всегда успешный обработчик
        worker = OutboxWorker(repo, handler, WorkerConfig(3, 10.millis)) // Воркер
        _ <- worker.processOnce // Запускаем один цикл
        list <- ref.get // Читаем состояние
      yield assertTrue(list.exists(e => e.id == "1" && e.status == "DONE")) // Проверяем DONE
    }, // Конец теста
    test("Given failing handler When attempts < max Then retry (no DLQ)") { // Повтор без DLQ
      for // Композиция эффектов
        ref <- Ref.make(List(OutboxEvent("2", "EMAIL", "{}", 0, "NEW"))) // Инициализация
        repo = InMemoryOutboxRepository(ref) // Репозиторий
        handler = MockEventHandler(_ => false) // Всегда падение
        worker = OutboxWorker(repo, handler, WorkerConfig(3, 10.millis)) // Воркер
        _ <- worker.processOnce // Один проход
        list <- ref.get // Считываем
      yield assertTrue(list.exists(e => e.id == "2" && e.attempts == 1 && e.status != "DLQ")) // Попытка увеличена
    }, // Конец теста
    test("Given failing handler When attempts reach max Then DLQ") { // DLQ при превышении
      for // Композиция эффектов
        ref <- Ref.make(List(OutboxEvent("3", "EMAIL", "{}", 2, "NEW"))) // Уже 2 попытки из 3
        repo = InMemoryOutboxRepository(ref) // Репозиторий
        handler = MockEventHandler(_ => false) // Падаем
        worker = OutboxWorker(repo, handler, WorkerConfig(3, 10.millis)) // Воркер
        _ <- worker.processOnce // Один проход
        list <- ref.get // Считываем
      yield assertTrue(list.exists(e => e.id == "3" && e.status == "DLQ")) // Должен быть DLQ
    } // Конец теста
  ) // Конец сьюта


