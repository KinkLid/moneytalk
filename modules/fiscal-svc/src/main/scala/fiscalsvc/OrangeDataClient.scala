// Пакет для fiscal-svc
package fiscalsvc // Объявляем пакет

// Импорт ZIO
import zio.* // Импорт ядра ZIO

// Контракт клиента OrangeData
trait OrangeDataClient: // Объявляем трейд клиента
  def createReceipt(payload: Map[String, Any]): Task[String] // Создаёт чек и возвращает идентификатор
  def getStatus(receiptId: String): Task[String] // Получает статус чека

// Мок-реализация клиента OrangeData
final class MockOrangeDataClient extends OrangeDataClient: // Мок-клиент
  def createReceipt(payload: Map[String, Any]): Task[String] = // Создание чека
    ZIO.succeed("rcpt_1") // Возвращаем фиксированный идентификатор
  def getStatus(receiptId: String): Task[String] = // Получение статуса
    ZIO.succeed("DONE") // Возвращаем статус DONE

// Тестовый слой для мок-клиента
object MockOrangeDataClient: // Объявляем объект
  val layer: ULayer[OrangeDataClient] = ZLayer.succeed(new MockOrangeDataClient) // Предоставляем слой

