// Пакет для admin-api моделей
package adminapi // Объявляем пакет

// Импорт Circe
import io.circe.* // Базовые типы
import io.circe.generic.semiauto.* // Полуавтоматическая деривация

// DTO инвентаря машины
final case class InventoryItem(machineId: String, detergentMl: Int, softenerMl: Int) // Поля инвентаря

// Ответ инвентаря
final case class InventoryResponse(items: List[InventoryItem]) // Список элементов

// Кодеки Circe
object JsonCodecs: // Объявляем объект кодеков
  given Encoder[InventoryItem] = deriveEncoder[InventoryItem] // Кодек элемента
  given Decoder[InventoryItem] = deriveDecoder[InventoryItem] // Декодер элемента
  given Encoder[InventoryResponse] = deriveEncoder[InventoryResponse] // Кодек ответа
  given Decoder[InventoryResponse] = deriveDecoder[InventoryResponse] // Декодер ответа

