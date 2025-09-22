// Пакет для lg-adapter
package lgadapter // Объявляем пакет

// Импорт ZIO
import zio.* // Импорт ядра ZIO

// Контракт клиента LG облака
trait LGCloudClient: // Объявляем трейд клиента
  def startCycle(machineId: String, programCode: String): Task[Unit] // Запуск цикла
  def status(machineId: String): Task[String] // Получение статуса

// Мок-реализация клиента
final class MockLGCloudClient extends LGCloudClient: // Мок-клиент
  def startCycle(machineId: String, programCode: String): Task[Unit] = // Реализация запуска
    ZIO.unit // Возвращаем успешный эффект
  def status(machineId: String): Task[String] = // Реализация статуса
    ZIO.succeed("IDLE") // Возвращаем строковый статус

// Слой для мок-клиента
object MockLGCloudClient: // Объект компаньон
  val layer: ULayer[LGCloudClient] = ZLayer.succeed(new MockLGCloudClient) // Предоставляем слой

