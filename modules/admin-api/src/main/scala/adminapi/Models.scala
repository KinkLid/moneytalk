// Пакет для admin-api моделей
package adminapi // Объявляем пакет

// Импорт Circe
import io.circe.* // Базовые типы
import io.circe.generic.semiauto.* // Полуавтоматическая деривация

// DTO инвентаря машины
final case class InventoryItem(machineId: String, detergentMl: Int, softenerMl: Int) // Поля инвентаря

// Ответ инвентаря
final case class InventoryResponse(items: List[InventoryItem]) // Список элементов

// Запрос принудительного старта машины
final case class ForceStartRequest(programCode: String) // Код программы для запуска

// Ответ принудительного старта/остановки
final case class CommandResponse(ok: Boolean, message: String) // Признак и сообщение

// Запрос ручного дозирования
final case class AdminDoseRequest(channel: String, ml: Int) // Канал и миллилитры

// Ответ по отчёту
final case class ReportResponse(reportDate: String, sent: Boolean) // Дата отчёта и факт отправки

// Кодеки Circe
object JsonCodecs: // Объявляем объект кодеков
  given Encoder[InventoryItem] = deriveEncoder[InventoryItem] // Кодек элемента
  given Decoder[InventoryItem] = deriveDecoder[InventoryItem] // Декодер элемента
  given Encoder[InventoryResponse] = deriveEncoder[InventoryResponse] // Кодек ответа
  given Decoder[InventoryResponse] = deriveDecoder[InventoryResponse] // Декодер ответа
  given Encoder[ForceStartRequest] = deriveEncoder[ForceStartRequest] // Кодек запроса старта
  given Decoder[ForceStartRequest] = deriveDecoder[ForceStartRequest] // Декодер запроса старта
  given Encoder[CommandResponse] = deriveEncoder[CommandResponse] // Кодек ответа команды
  given Decoder[CommandResponse] = deriveDecoder[CommandResponse] // Декодер ответа команды
  given Encoder[AdminDoseRequest] = deriveEncoder[AdminDoseRequest] // Кодек дозирования
  given Decoder[AdminDoseRequest] = deriveDecoder[AdminDoseRequest] // Декодер дозирования
  given Encoder[ReportResponse] = deriveEncoder[ReportResponse] // Кодек отчёта
  given Decoder[ReportResponse] = deriveDecoder[ReportResponse] // Декодер отчёта

