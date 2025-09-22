// Определяем пакет common для общих компонентов
package common // Объявляем пакет

// Импортируем ZIO
import zio.* // Импорт ядра ZIO

// Тип события outbox
final case class OutboxEvent(id: String, eventType: String, payloadJson: String, attempts: Int, status: String) // Поля события outbox

// Репозиторий outbox
trait OutboxRepository: // Контракт репозитория
  def fetchNext(batchSize: Int): UIO[List[OutboxEvent]] // Выборка пачки новых событий
  def markProcessing(id: String): UIO[Unit] // Перевод в статус PROCESSING
  def markDone(id: String): UIO[Unit] // Перевод в статус DONE
  def markDlq(id: String): UIO[Unit] // Перевод в статус DLQ
  def incAttempts(id: String): UIO[Int] // Увеличение счётчика попыток

// Обработчик одного события по типу
trait EventHandler: // Контракт обработчика событий
  def handle(event: OutboxEvent): Task[Unit] // Обработка события

// Настройки воркера
final case class WorkerConfig(maxAttempts: Int, pollInterval: Duration) // Максимум попыток и период опроса

// Воркер обработки outbox
final class OutboxWorker(repo: OutboxRepository, handler: EventHandler, cfg: WorkerConfig): // Реализация воркера
  // Один цикл обработки
  def processOnce: Task[Unit] = // Процессинг одной пачки
    for // Начинаем композицию эффектов
      batch <- repo.fetchNext(batchSize = 50) // Загружаем события
      _ <- ZIO.foreach(batch) { ev => // Идём по каждому событию
        for // Обрабатываем событие
          _ <- repo.markProcessing(ev.id) // Переводим в PROCESSING
          res <- handler.handle(ev).either // Пытаемся обработать
          _ <- res match // Разбираем результат
            case Right(_) => repo.markDone(ev.id) // Успех -> DONE
            case Left(_) => // Ошибка обработки
              repo.incAttempts(ev.id).flatMap { n => // Увеличиваем попытки
                if n >= cfg.maxAttempts then repo.markDlq(ev.id) // Превышен лимит -> DLQ
                else ZIO.unit // Иначе оставляем для повторной обработки
              } // Конец обработки ошибки
        yield () // Возвращаем Unit
      } // Завершаем обход
    yield () // Возвращаем Unit

  // Бесконечный цикл с периодическим опросом
  def runForever: Task[Nothing] = // Запуск воркера в цикле
    processOnce.ignore *> ZIO.sleep(cfg.pollInterval) *> runForever // Обрабатываем, спим и повторяем

// Простая in-memory реализация репозитория для тестов
final class InMemoryOutboxRepository(state: Ref[List[OutboxEvent]]) extends OutboxRepository: // Памятная реализация
  def fetchNext(batchSize: Int): UIO[List[OutboxEvent]] = // Выборка новой пачки
    state.get.map(_.filter(_.status == "NEW").take(batchSize)) // Берём только NEW
  def markProcessing(id: String): UIO[Unit] = // Помечаем PROCESSING
    state.update(_.map(e => if e.id == id then e.copy(status = "PROCESSING") else e)) // Обновляем статус
  def markDone(id: String): UIO[Unit] = // Помечаем DONE
    state.update(_.map(e => if e.id == id then e.copy(status = "DONE") else e)) // Обновляем статус
  def markDlq(id: String): UIO[Unit] = // Помечаем DLQ
    state.update(_.map(e => if e.id == id then e.copy(status = "DLQ") else e)) // Обновляем статус
  def incAttempts(id: String): UIO[Int] = // Инкремент попыток
    for // Композиция эффектов
      n <- state.modify { list => // Модифицируем список
        val updated = list.map { e => // Проходим по событиям
          if e.id == id then e.copy(attempts = e.attempts + 1) else e // Инкрементируем нужное
        } // Конец обновления
        val attempts = updated.find(_.id == id).map(_.attempts).getOrElse(0) // Получаем новое значение
        (attempts, updated) // Возвращаем пару
      } // Конец modify
    yield n // Возвращаем число попыток

// Мок-обработчик, который может падать в зависимости от типа события
final class MockEventHandler(f: OutboxEvent => Boolean) extends EventHandler: // Мок обработчика
  def handle(event: OutboxEvent): Task[Unit] = // Обработка
    if f(event) then ZIO.unit else ZIO.fail(new RuntimeException("fail")) // Успех или ошибка


